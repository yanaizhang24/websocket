package cn.yanf.websocket;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2016/11/11.
 */
@ServerEndpoint(value="/websocket/chat")
public class ChatEntpoint {
    private static final String GUEST_PREFIX="访客";
    private static final AtomicInteger connectionIds=new AtomicInteger(0);
    private static final Set<ChatEntpoint> clientSet= new CopyOnWriteArraySet<>();
    private final String nickname;//昵称
    private Session session;
    public ChatEntpoint(){
        nickname=GUEST_PREFIX+connectionIds.getAndIncrement();//获取并加一
    }
    @OnOpen
    public void start(Session session){
        this.session =session;
        clientSet.add(this);
        String message=String.format("[%s%s]",nickname,"加入聊天室！");
        broadcast(message);
    }
    @OnClose
    public void end(){
        clientSet.remove(this);
        String message=String.format("%s:%s",nickname,"离开了聊天室！");
        broadcast(message);
    }
    @OnMessage
    public void incoming(String message){
        String filteredMessage=String.format("%s:%s",nickname,filter(message));
        broadcast(filteredMessage);
    }
    @OnError
    public void onError(Throwable throwable) throws Throwable{
        System.out.println("WebSocket服务器错误"+throwable);
    }
    private static void broadcast(String msg){
        for(ChatEntpoint client:clientSet){
            synchronized (client){
                try {
                    client.session.getBasicRemote().sendText(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("聊天错误，向客户端 "+client+" 发送消息出现错误。");
                    try {
                        client.session.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    String message=String.format("[%s %s]",client.nickname,"已经被断开了连接。");
                    broadcast(message);
                }
            }
        }
    }
    private static String filter(String message){
        if(message==null){
            return null;
        }
        char content[]=new char[message.length()];
        message.getChars(0,message.length(),content,0);
        StringBuilder result=new StringBuilder(content.length+50);
        for(int i=0;i<content.length;i++){
            switch(content[i]){
                case '<':result.append("&lt;");break;
                case '>':result.append("&gt;");break;
                case '&':result.append("&amp;");break;
                case '"':result.append("&quot;");break;
                default:result.append(content[i]);
            }
        }
        return (result.toString());
    }
}
