package com.yx.framework.net.ws;

import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import com.neovisionaries.ws.client.WebSocketListener;
import com.neovisionaries.ws.client.WebSocketState;
import com.yx.framework.utils.ThreadManager;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;


/**
 * function:WebSocket实例管理器,WebSocket信息收发
 * <p>
 * <p>
 * Created by Leo on 2018/11/12.
 */
@SuppressWarnings("WeakerAccess")
public class WSManager {
    public static final int TIME_EXPIRE_PUSH = 60*60;//60分钟
    private static final int TIME_OUT_CONNECT = 1000;//连接超时
    private static final long HEARTBEAT_INTERVAL = 3000;//心跳间隔
    private static final long RECONNECT_INTERVAL = 1000;//重连间隔
    private static final long PONG_IME_OUT_CONNECT = 3000;//pong超时
    private static final String HEARTBEAT_CHAR = "ping";
    private static final AtomicReference<WSManager> INSTANCE = new AtomicReference<>( );
    private volatile long pongTime = System.currentTimeMillis( );
    private final BlockingQueue<String> mRequestQueue = new LinkedBlockingQueue<>( );
    private final ThreadManager.ThreadPoolProxy mThreadPoolProxy = ThreadManager.getShortPool( );
    private volatile long mLastUpdateTime;
    private volatile int countReconnect;
    private WebSocket mWebSocket;
    private Handler mHandlerThread;
    private String URL_WS;

    public static WSManager instance() {
        for (; ; ) {
            WSManager instance = INSTANCE.get( );
            if (instance != null) return instance;
            instance = new WSManager( );
            if (INSTANCE.compareAndSet(null, instance)) return instance;
        }
    }

    private WSManager() {
        WSLog.d("WSManager");
        HandlerThread handlerThread = new HandlerThread("Thread-WSManager");
        handlerThread.start( );
        mHandlerThread = new Handler(handlerThread.getLooper( ));
    }

    /**
     * 连接ws
     */
    public  synchronized void connect() {
        WSLog.d("connect");
        try {
            URL_WS = "ws://192.168.1.183:8080/websocket/t2tt";
            WSLog.d("connecting...url=" + URL_WS);
            mWebSocket = new WebSocketFactory( )
                    .createSocket(URL_WS, TIME_OUT_CONNECT)
                    .setAutoFlush(true)//自动刷新
                    .setMissingCloseFrameAllowed(false)//设置不允许服务端关闭连接却未发送关闭帧
                    .removeListener(mWebSocketListener).addListener(mWebSocketListener)//添加回调监听
                    .connectAsynchronously( );
        } catch (Exception e) {
            WSLog.e(e.getMessage( ));
        }
    }


    /**
     * 重连ws
     */
    public synchronized void reconnect() {
        WSLog.d("reconnect: mState " + mState);
        //当前连接断开了  //不是正在重连状态
        if (mWebSocket != null && !mWebSocket.isOpen( ) && mState != WebSocketState.CONNECTING) {
            countReconnect = Math.max(countReconnect + 1, 1);
            WSLog.d(String.format(Locale.getDefault( ), "%ds后开始第%d次重连 -- url=%s", RECONNECT_INTERVAL / 1000, countReconnect, URL_WS));
            WSLog.d("reconnect countReconnect " + countReconnect);
            if (countReconnect > 2) {
                cancelReconnect( );
            } else {
                mHandlerThread.postDelayed(mReconnectTask, RECONNECT_INTERVAL);
            }
        }
    }


    public void sendReq(String msg) {
        WSLog.d("sendReq");
        if (mWebSocket != null && mWebSocket.isOpen( )) {
            mRequestQueue.add(msg);
        }
    }

    /**
     * 断开连接
     */
    public synchronized void disconnect() {
        WSLog.d("disconnect");
        mHandlerThread.removeCallbacks(mHeartBeatTask);
        mRequestQueue.clear( );
        if (mWebSocket != null) mWebSocket.disconnect( );
        cancelReconnect( );//取消重连任务
    }

    /**
     * 是否处于连接中
     */
    public synchronized boolean isConnected() {
        return mWebSocket != null && mWebSocket.isOpen( );
    }

    private volatile WebSocketState mState;

    //WebSocket监听
    private final WebSocketListener mWebSocketListener = new WebSocketAdapter( ) {
        @Override
        public void onStateChanged(WebSocket websocket, WebSocketState newState) {
            WSLog.v("onStateChanged: oldState=" + mState + ", newState=" + newState);
            mState = newState;
        }

        @Override
        public void onTextMessage(WebSocket websocket, final String text) {
            WSLog.i("onTextMessage: " + text);
            WSLog.d("ResponseDispatchTask: dispatch msg. pushType=" );
        }


        @Override
        public void onSendError(WebSocket websocket, WebSocketException cause, WebSocketFrame frame) {
            try {
                WSLog.e("onSendError: " + cause.getError( ).toString( ) + ", exception=" + cause.getMessage( ));
                WSLog.e("onSendError: WebSocketFrame=" + frame.toString( ));
            } catch (Exception e) {
                WSLog.e(e.getMessage( ));
                WSLog.e("onSendError: " + e.getMessage( ));
            }
        }

        @Override
        public void onMessageError(WebSocket websocket, WebSocketException cause, List<WebSocketFrame> frames) {
            try {
                WSLog.e("onMessageError: " + cause.getError( ).toString( ) + ", exception=" + cause.getMessage( ));
            } catch (Exception e) {
                WSLog.e(e.getMessage( ));
            }
        }

        @Override
        public void onConnected(WebSocket websocket, Map<String, List<String>> headers) {
            websocket.sendText("{\"message\":\"我来啦啦啦啦\",\"username\":\"t2tt\",\"to\":\"ttt\"}");
            try {
                WSLog.d("WebSocket connected.");
                //发送心跳包
                mHandlerThread.removeCallbacks(mHeartBeatTask);
                //取消重连任务
                cancelReconnect( );
                mThreadPoolProxy.execute(mSendRequestTask);
                //发布连接事件
            } catch (Exception e) {
                WSLog.e(e.getMessage( ));
                WSLog.e("onConnected: " + e.getMessage( ));
            }
        }

        @Override
        public void onConnectError(WebSocket websocket, WebSocketException exception) {
            WSLog.d("onConnectError: ");
            try {
                WSLog.e("onConnectError: error=" + exception.getError( ).toString( ) + ", exception=" + exception.getMessage( ));
            } catch (Exception e) {
                WSLog.e(e.getMessage( ));
                WSLog.e("onConnectError: " + e.getMessage( ));
            }
            reconnect( );//连接错误的时候调用重连方法
        }

        @Override
        public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) {
            //移除任务
            WSLog.d("onDisconnected: ");
            mThreadPoolProxy.cancel(mSendRequestTask);
            WSLog.d("WebSocket disconnected. closedByServer:" + closedByServer);
        }
    };

    /**
     * 取消重连
     */
    public void cancelReconnect() {
        WSLog.d("cancelReconnect");
        pongTime = System.currentTimeMillis( );
        countReconnect = 0;
        mHandlerThread.removeCallbacks(mReconnectTask);
    }

    /**
     * 发送消息队列任务
     */
    private final Runnable mSendRequestTask = new Runnable( ) {
        @Override
        public void run() {
            while (mState == WebSocketState.OPEN) {
                try {
                    String request = mRequestQueue.take( );
                    if (!TextUtils.isEmpty(request)) {
                        if (mWebSocket != null && mWebSocket.isOpen( )) {
                            WSLog.d("ws-request: " + request);
                            mWebSocket.sendText(request);
                            mWebSocket.flush( );
                        } else {
                            reconnect( );
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread( ).interrupt( );
                    WSLog.e(e.toString( ));
                    WSLog.e("mSendRequestTask: " + e.getMessage( ));
                }
            }
        }
    };


    /**
     * 重新连接
     */
    private Runnable mReconnectTask = this::connect;



    /**
     * 心跳包任务
     */
    private Runnable mHeartBeatTask = new Runnable( ) {
        @Override
        public void run() {
            if (mWebSocket != null && mWebSocket.isOpen( )) {
                WSLog.v("HeartBeat...send:" + HEARTBEAT_CHAR);
                long pingTime = System.currentTimeMillis( );
                if (pingTime - pongTime > PONG_IME_OUT_CONNECT) {//表示超过3秒没收到pong了
                    WSLog.d("PONG_IME_OUT_CONNECT2");
                    disconnect( );
                    connect();
                } else {
                    mWebSocket.sendText(HEARTBEAT_CHAR);
                    mWebSocket.flush( );
                }
            } else {
            }

        }
    };


}
