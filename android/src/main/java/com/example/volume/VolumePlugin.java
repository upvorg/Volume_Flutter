package com.example.volume;

import android.content.Context;
import android.media.AudioManager;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * VolumePlugin
 */
public class VolumePlugin implements FlutterPlugin, MethodCallHandler {

    private MethodChannel channel;
    private AudioManager audioManager;
    private int streamType;
    private Context context;

    /**
     * v2 plugin embedding
     */
    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
        context = binding.getApplicationContext();

        channel = new MethodChannel(binding.getBinaryMessenger(), "volume");
        channel.setMethodCallHandler(this);
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
        channel = null;
    }

    /**
     * Deprecated plugin registration.
     */
    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "volume");
        channel.setMethodCallHandler(new VolumePlugin());
    }

    @Override
    public void onMethodCall(MethodCall call, @NonNull Result result) {
        if (call.method.equals("controlVolume")) {
            int i = call.argument("streamType");
            streamType = i;
            result.success(null);
        } else if (call.method.equals("getMaxVol")) {
            result.success(getMaxVol());
        } else if (call.method.equals("getVol")) {
            result.success(getVol());
        } else if (call.method.equals("setVol")) {
            int i = call.argument("newVol");
            int showUiFlag = call.argument("showVolumeUiFlag");
            setVol(i,showUiFlag);
            result.success(0);
        } else {
            result.notImplemented();
        }
    }

    private void initAudioManager() {
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    private int getMaxVol() {
        initAudioManager();
        return audioManager.getStreamMaxVolume(streamType);
    }

    private int getVol() {
        initAudioManager();
        return audioManager.getStreamVolume(streamType);
    }

    private int setVol(int i, int showVolumeUiFlag) {
        initAudioManager();
        audioManager.setStreamVolume(streamType, i, showVolumeUiFlag);
        return audioManager.getStreamVolume(streamType);
    }
}