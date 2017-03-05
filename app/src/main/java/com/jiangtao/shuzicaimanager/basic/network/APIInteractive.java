package com.jiangtao.shuzicaimanager.basic.network;


import com.blankj.utilcode.utils.LogUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by nicky on 2017/1/4.
 * 所有的api请求
 */

public class APIInteractive {

    //请求对象
    private static APICollections request = null;

    //初始化
    public static void initRetrofit() {
        if (null == request) {
            request = NetworkRequest.getRetrofitClient(APICollections.class);
        }
    }

    /**
     * 获取所有APP的信息
     *
     * @param callback
     */
    public static void getAllAPPInfos(final INetworkResponse callback) {
        if (null == request) {
            initRetrofit();
        }
        Call<JsonObject> call = request.getApps();
        NetworkRequest.netRequest(call, callback);
    }


    /**
     * 获取指数信息
     *
     * @param where
     * @param callback
     */
    public static void getIndexData(String where, final INetworkResponse callback) {
        if (null == request) {
            initRetrofit();
        }

        Call<JsonObject> call = request.getIndexDate(where);
        NetworkRequest.netRequest(call, callback);
    }

    /**
     * 获取指数信息
     *
     * @param callback
     */
    public static void getBillboardData(final INetworkResponse callback) {
        if (null == request) {
            initRetrofit();
        }

        Call<JsonObject> call = request.getBillboardData(10, 0);
        NetworkRequest.netRequest(call, callback);
    }


    /**
     * 请求验证码
     *
     * @param body
     * @param callback
     */
    public static void requestSmsCode(RequestBody body, final INetworkResponse callback) {
        if (null == request) {
            initRetrofit();
        }

        Call<JsonObject> call = request.requestSmsCode(body);
        NetworkRequest.netRequest(call, callback);
    }

    /**
     * 查询验证码
     *
     * @param ssid
     * @param callback
     */
    public static void querySms(String ssid, final INetworkResponse callback) {
        if (null == request) {
            initRetrofit();
        }

        Call<JsonObject> call = request.querySms(ssid);
        NetworkRequest.netRequest(call, callback);
    }

    /**
     * 查询验证码是否存在
     *
     * @param where
     * @param callback
     */
    public static void getVerifyCodeExist(String where, final INetworkResponse callback) {
        if (null == request) {
            initRetrofit();
        }

        Call<JsonObject> call = request.isInvitationCodeExist(where);
        NetworkRequest.netRequest(call, callback);
    }

    /**
     * 登录
     *
     * @param account
     * @param password
     * @param callback
     */
    public static void startLogin(String account, String password, final INetworkResponse callback) {
        if (null == request) {
            initRetrofit();
        }
        Call<JsonObject> call = request.login(account, password);
        NetworkRequest.netRequest(call, callback);
    }

    /**
     * 获取当前用户信息
     *
     * @param objectID
     * @param callback
     */
    public static void getCurrentUser(String objectID, final INetworkResponse callback) {
        if (null == request) {
            initRetrofit();
        }
        Call<JsonObject> call = request.getCurrentUser(objectID);
        NetworkRequest.netRequest(call, callback);
    }

    /**
     * 获取当前用户的财富
     *
     * @param where
     * @param callback
     */
    public static void getWealthValue(String where, final INetworkResponse callback) {
        if (null == request) {
            initRetrofit();
        }
        Call<JsonObject> call = request.getWealthValue(where);
        NetworkRequest.netRequest(call, callback);
    }

    /**
     * 获取商品信息
     *
     * @param limit
     * @param skip
     * @param callback
     */
    public static void getGoods(int limit, int skip, final INetworkResponse callback) {
        if (null == request) {
            initRetrofit();
        }
        Call<JsonObject> call = request.getGoods(limit, skip);
        NetworkRequest.netRequest(call, callback);
    }


    /**
     * 获取服务器时间
     *
     * @param callback
     */
    public static void getServerTime(final INetworkResponse callback) {
        if (null == request) {
            initRetrofit();
        }
        Call<JsonObject> call = request.getServerTime();
        NetworkRequest.netRequest(call, callback);
    }

    /**
     * 获取交易记录
     *
     * @param where
     * @param callback
     */
    public static void getExchangeRecord(String where, String include, final INetworkResponse callback) {
        if (null == request) {
            initRetrofit();
        }
        Call<JsonObject> call = request.getExchangeRecord(where, include);
        NetworkRequest.netRequest(call, callback);
    }


    /**
     * 获取交易记录
     *
     * @param where
     * @param callback
     */
    public static void getWealthDetailRecord(String where, final INetworkResponse callback) {
        if (null == request) {
            initRetrofit();
        }
        Call<JsonObject> call = request.getWealthDetailRecord(where);
        NetworkRequest.netRequest(call, callback);
    }

    /**
     * 批量操作
     * @param bean
     * @param callback
     */
    public static void bmobBatch(Map<String, List> bean, final INetworkResponse callback) {
        if (null == request) {
            initRetrofit();
        }
        Call<JsonArray> call = request.bmobBatch(bean);
        NetworkRequest.netRequest2(call, callback);
    }


    /**
     * 获取用户信息
     *
     * @param where
     * @param callback
     */
    public static void getUserInfo(String where, final INetworkResponse callback) {
        if (null == request) {
            initRetrofit();
        }
        Call<JsonObject> call = request.getUserInfo(where);
        NetworkRequest.netRequest(call, callback);
    }


    /**
     * 上传文件
     *
     * @param path
     * @param callback
     */
    public static void postFile(String path, final INetworkResponse callback) {
        LogUtils.i("开始上传文件：" + path);
        BmobUpload.initBmob("cd89b563ca70dfd60befb89fa9ad6e42",
                "f21c0ff7f6aa0405e9f97c30fc9a414f", 6 * 1000);
        BmobUpload.uploadFile(path, new BmobUpload.IUpLoadResult() {
            @Override
            public void onUpLoadResult(String result) {
                LogUtils.i("上传完成，result：" + result);
                if (!result.equals("Not Found") && !result.equals("file Not Found")
                        && !result.equals("Error") && !result.equals("Unregistered")
                        && !result.equals("")) {
                    try {
                        //将字符串转换成jsonObject对象
                        JSONObject myJsonObject = new JSONObject(result);
                        callback.onSucceed(myJsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        callback.onFailure(INetworkResponse.ERR_RESULT_FAILURE);
                    }
                } else {
                    callback.onFailure(INetworkResponse.ERR_RESULT_FAILURE);
                }
            }
        });

    }
}
