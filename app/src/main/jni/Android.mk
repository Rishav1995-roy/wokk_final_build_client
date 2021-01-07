LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := Wokk
LOCAL_SRC_FILES := Wokk.c

include $(BUILD_SHARED_LIBRARY)