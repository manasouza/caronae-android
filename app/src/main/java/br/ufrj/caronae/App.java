package br.ufrj.caronae;

import android.content.Context;
import android.content.Intent;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orm.SugarApp;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import br.ufrj.caronae.ACRAreport.CrashReportFactory;
import br.ufrj.caronae.ACRAreport.CrashReportSender;
import br.ufrj.caronae.acts.LoginAct;
import br.ufrj.caronae.asyncs.LogOut;
import br.ufrj.caronae.httpapis.ChatService;
import br.ufrj.caronae.httpapis.NetworkService;
import br.ufrj.caronae.models.User;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import org.acra.*;
import org.acra.annotation.*;

/** Usa o Falae para reportar crashes **/
//@ReportsCrashes(
//        reportSenderFactoryClasses = {CrashReportFactory.class}
//)
/******/

@ReportsCrashes(mailTo = "reports@yourdomain.com",
        customReportContent = {ReportField.STACK_TRACE},
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.crash_toast_text)

public class App extends SugarApp {

    private static final String DEV_SERVER_ENDPOINT          = "https://dev.caronae.tic.ufrj.br/";
    private static final String DEV_SERVER_IP                = "http://54.243.7.207/";
    private static final String MEUDIGOCEAN_PROD_ENDPOINT    = "http://45.55.46.90:80/";
    private static final String MEUDIGOCEAN_DEV_ENDPOINT     = "http://45.55.46.90:8080/";
    private static final String LOCAL_SERV_ENDPOINT          = "http://home.meriw.xyz:8001";
    private static final String TIC_ENDPOINT                 = "https://api.caronae.ufrj.br/";

    //TODO: ADD FIREBASE KEY
    private static final String FIREBASE_ENDPOINT = "fcm.googleapis.com/fcm";
    private static final String FIREBASE_API_KEY = "AIzaSyApLZKt5uG2GjQtfiAN5n2kFYjvapJag-g";

    // TODO: REMOVE OLD GCM CODE
//    private static final String GCM_ENDPOINT = "https://android.googleapis.com/gcm";
//    private static final String GCM_API_KEY = "AIzaSyBtGz81bar_LcwtN_fpPTKRMBL5glp2T18";

    private static App inst;
    private static User user;
    private static NetworkService networkService;
    private static ChatService chatService;
    private static MainThreadBus bus;

    public App() {
        inst = this;
    }

    public static App inst() {
        return inst;
    }

    public static boolean isUserLoggedIn() {
        return getUser() != null;
    }

    public static void clearUserVar() {
        user = null;
    }

    public static User getUser() {
        if (user == null) {
            String userJson = SharedPref.getUserPref();
            if (!userJson.equals(SharedPref.MISSING_PREF))
                user = new Gson().fromJson(userJson, User.class);
        }

        return user;
    }

    public static String getHost() {
        //return MEUDIGOCEAN_DEV_ENDPOINT;
        //return MEUDIGOCEAN_PROD_ENDPOINT;
//        return DEV_SERVER_IP;
        // TODO: Lembrar de remover Crash Report ACRA pro servidor de producao
        return DEV_SERVER_ENDPOINT;
//        return TIC_ENDPOINT;
    }

    public static NetworkService getNetworkService(final Context context) {

        if (networkService == null) {
            String endpoint = getHost();

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.readTimeout(30, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS);
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    if (App.isUserLoggedIn()) {

                        Request request = original.newBuilder()
                                .header("Content-Type", "application/json")
                                .header("token", SharedPref.getUserToken())
                                .header("User-Agent", Util.getHeaderForHttp(context))
                                .method(original.method(), original.body())
                                .build();

                        Response response = chain.proceed(request);

                        return response;
                    }
                    Request request = original.newBuilder()
                            .header("Content-Type", "application/json")
                            .header("User-Agent", Util.getHeaderForHttp(context))
                            .method(original.method(), original.body())
                            .build();
                    Response response = chain.proceed(request);
                    return response;
                }
            });

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            httpClient.addInterceptor(logging);

            OkHttpClient client = httpClient.build();

            networkService = new Retrofit.Builder()
                    .baseUrl(endpoint)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build()
                    .create(NetworkService.class);

        }
        return networkService;
    }

    public static ChatService getChatService(final Context context) {
        if (chatService == null) {
            String endpoint = getHost();

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    if (App.isUserLoggedIn()) {

                        Request request = original.newBuilder()
                                .header("Content-Type", "application/json")
                                .header("token", SharedPref.getUserToken())
                                .header("User-Agent", Util.getHeaderForHttp(context))
                                .method(original.method(), original.body())
                                .build();

                        Response response = chain.proceed(request);

                        return response;
                    }
                    Request request = original.newBuilder()
                            .header("Content-Type", "application/json")
                            .header("User-Agent", Util.getHeaderForHttp(context))
                            .method(original.method(), original.body())
                            .build();
                    Response response = chain.proceed(request);
                    return response;
                }
            });

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            httpClient.addInterceptor(logging);

            OkHttpClient client = httpClient.build();

            chatService = new Retrofit.Builder()
                    .baseUrl(endpoint)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build()
                    .create(ChatService.class);

        }
        return chatService;
    }

    public static MainThreadBus getBus() {
        if (bus == null) {
            bus = new MainThreadBus();
        }

        return bus;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        ACRA.init(this);
    }

    public static void LogOut(){
        FirebaseMessaging.getInstance().unsubscribeFromTopic(SharedPref.TOPIC_GERAL);
        FirebaseMessaging.getInstance().unsubscribeFromTopic(App.getUser().getDbId() + "");
        new LogOut(App.inst()).execute();
        Intent intent = new Intent(App.inst(), LoginAct.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        App.inst().startActivity(intent);
    }
}
