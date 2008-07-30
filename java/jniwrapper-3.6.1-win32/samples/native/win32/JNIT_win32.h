/*
 * Copyright 2000-2005 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL. 
 * Use is subject to license terms.
 */
#ifndef __JNIT_WIN32_H
#define __JNIT_WIN32_H

#define declexport(rettype) extern "C" __declspec(dllexport) rettype

#define jni_int8 char
#define jni_uint8 unsigned char
#define jni_int16 short
#define jni_uint16 unsigned short
#define jni_int32 long
#define jni_uint32 unsigned long
#define jni_int64 __int64

#define jni_singlefloat float
#define jni_doublefloat double

#define STDCALL __stdcall

#endif
