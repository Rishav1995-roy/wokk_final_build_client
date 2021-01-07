#include <jni.h>

#define SIZE 101



JNIEXPORT jstring JNICALL
Java_com_app_wokk_retrofit_Constant_getBaseURL(JNIEnv *env, jclass instance) {

    char a1 = 'h';
    char a2 = 't';
    char a3 = 't';
    char a4 = 'p';
    char a5 = 's';
    char a6 = ':';
    char a7 = '/';
    char a8 = '/';
    char a9 = 'w';
    char a10 = 'o';
    char a11 = 'k';
    char a12 = 'k';
    char a13 = '.';
    char a14 = 'c';
    char a15 = 'o';
    char a16 = '.';
    char a17 = 'i';
    char a18 = 'n';
    char a19 = '/';
    char a20 = 'a';
    char a21 = 'p';
    char a22 = 'i';
    char a23 = '/';
    char key[SIZE] = {a1, a2, a3, a4, a5, a6, a7, a8, a9, a10,
                      a11, a12, a13, a14, a15, a16, a17, a18, a19,a20,a21,a22,a23, '\0'};
    return (*env)->NewStringUTF(env, key);
}


JNIEXPORT jstring JNICALL
Java_com_app_wokk_retrofit_Constant_getUsername(JNIEnv *env, jobject instance) {
    char a = 's';
    char b = 'a';
    char c = 'r';
    char d = 'a';
    char e = 's';
    char f = 'i';
    char g = 'j';
    char h = '9';
    char i = '4';
    char key[SIZE] = {a, b, c, d, e, f, g, h,i, '\0'};
    return (*env)->NewStringUTF(env, key);
}

JNIEXPORT jstring JNICALL
Java_com_app_wokk_retrofit_Constant_getPassword(JNIEnv *env, jobject instance) {
    char a = '1';
    char b = '2';
    char c = '3';
    char d = 'x';
    char e = 'd';
    char f = 'M';
    char g = 't';
    char h = '6';
    char key[SIZE] = {a, b, c, '\0'};
    return (*env)->NewStringUTF(env, key);
}