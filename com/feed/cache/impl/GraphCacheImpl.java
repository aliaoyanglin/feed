package com.feed.cache.impl;

import com.feed.cache.GraphCache;
import com.feed.model.Attention;
import com.feed.model.Friend;
import net.spy.memcached.MemcachedClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GraphCacheImpl implements GraphCache {
    private MemcachedClient mcc;
    private String GraphMcHost;
    private int GraphMcPort;

    public GraphCacheImpl(String GraphMcHost, int GraphMcPort) {
        this.GraphMcHost = GraphMcHost;
        this.GraphMcPort = GraphMcPort;
        try {
            this.mcc = new MemcachedClient(new InetSocketAddress(GraphMcHost,GraphMcPort));
        }catch (IOException e){
            System.out.println(String.format("connect to mc: %s:%d failed: e", GraphMcHost, GraphMcPort, e));
            throw new UnsupportedOperationException("mc no available - " + GraphMcHost + ":" + GraphMcPort);
        }
    }

    public GraphCacheImpl(){}

    @Override
    public void addFriend(Friend friend) {
        if (getFriends(friend.getUid()) != null) {
            //取出mc里的数据，准备合并
            Friend friendMc = getFriends(friend.getUid());
//            List<Attention> friendSwap = friend.getAttention();//创建一个新的List，用于存储,合并和去重
//            friendSwap.addAll(friendMc.getAttention());
            //合并完成后进行去重，提前在Attention中进行@override equals()方法
            for(Attention att : friend.getAttention()){
                if(!friendMc.getAttention().contains(att.getUid())){
                    friendMc.getAttention().add(att);
                }
            }


//            friendMc.setAttention(friendSwap.stream().distinct().collect(Collectors.toList()));

            mcc.set("friend" + friend.getUid() + "", 3600, friendMc.toJason());
        }
        if(getFriends(friend.getUid()) == null){
            mcc.set("friend"+ friend.getUid() +"", 3600,friend.toJason());
        }
    }

    @Override
    public void deleteFriend(Friend friend) {
        if(getFriends(friend.getUid())!=null) {
            Friend friendMc = getFriends(friend.getUid());

            List<Attention> friendSwap = friendMc.getAttention();
            for(Attention attention:friend.getAttention())
                for(int i = friendSwap.size()-1;i>=0;i--)
                    if(attention.getUid() == friendSwap.get(i).getUid())
                        friendSwap.remove(i);
            friendMc.setAttention(friendSwap);

            mcc.set("friend" + friend.getUid() + "", 3600, friendMc.toJason());
        }
    }

    @Override
    public Friend getFriends(long id) {
        if (Friend.parseJason(String.valueOf(mcc.get("friend" + id))) != null)
            return Friend.parseJason(String.valueOf(mcc.get("friend" + id)));
        else return null;
    }

    @Override
    public void addFollowing(Friend friend) {
        if (getFollowing(friend.getUid()) != null) {
            //取出mc里的数据，准备合并
            Friend friendMc = getFriends(friend.getUid());
            List<Attention> friendSwap = friend.getAttention();//创建一个新的List，用于存储,合并和去重
            friendSwap.addAll(friendMc.getAttention());
            //合并完成后进行去重，提前在Attention中进行@override equals() hashset()方法
            friendMc.setAttention(friendSwap.stream().distinct().collect(Collectors.toList()));

            mcc.set("following" + friend.getUid() + "", 3600, friendMc.toJason());
        }
        if(getFriends(friend.getUid()) == null){
            mcc.set("following"+ friend.getUid() +"", 3600,friend.toJason());
        }
    }

    @Override
    public void deleteFollowing(Friend friend) {
        for(int i = 0;i<friend.getAttention().size();i++) {
            if(getFollowing(friend.getUid())!=null) {
                Attention attention = new Attention(friend.getUid(), friend.getAttention().get(i).getCreated());
                List<Attention> attentionList = new ArrayList<>();
                attentionList.add(attention);
                Friend friend1 = new Friend(friend.getAttention().get(i).getUid(), attentionList);

                Friend friendMc = getFollowing(friend.getUid());

                List<Attention> friendSwap = friendMc.getAttention();
                friendSwap.removeAll(friend1.getAttention());

                friendMc.setAttention(friendSwap);
                mcc.set("following" + friend.getUid() + "", 3600, friendMc.toJason());
            }
        }
    }

    @Override
    public Friend getFollowing(long id) {
        if (Friend.parseJason(String.valueOf(mcc.get("following" + id))) != null)
            return Friend.parseJason(String.valueOf(mcc.get("following" + id)));
        else return null;
    }
}