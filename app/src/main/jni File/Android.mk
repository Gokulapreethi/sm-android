# $Id$

LOCAL_PATH	:= $(call my-dir)
include $(CLEAR_VARS)

# Get PJ build settings
include ../../../../build.mak
include $(PJDIR)/build/common.mak


# Constants
MY_JNI_DIR	:= jni

# Android build settings
LOCAL_MODULE    := libpjsip
LOCAL_CFLAGS    += -g -Werror $(APP_CFLAGS)
LOCAL_LDFLAGS   := $(APP_LDFLAGS)
LOCAL_LDLIBS    += -L$(SYSROOT)/usr/lib -llog  $(APP_LDLIBS)
LOCAL_SRC_FILES :=pjsua_app.c \



include $(BUILD_SHARED_LIBRARY)

