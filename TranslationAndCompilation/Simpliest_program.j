.class public Simpliest_program
.super java/lang/Object 

.method public <init>()V
	aload_0
	invokespecial java/lang/Object/<init>()V
	return
.end method

.method public static sum(DD)D
	.limit stack 4
	.limit locals 6
.var 2 is b D from LABEL_0 to LABEL_1
.var 0 is a D from LABEL_0 to LABEL_1

LABEL_0:

.var 4 is d D from LABEL_0 to LABEL_1
	ldc 0
	i2d
	dstore 4
#d_equation:
	dload 0
	dload 2
	dadd
	dstore 4
#return_stmt:
	dload 4
	dreturn
LABEL_1:


.end method

.method public static mult(II)I
	.limit stack 2
	.limit locals 2
.var 1 is b I from LABEL_0 to LABEL_1
.var 0 is a I from LABEL_0 to LABEL_1

LABEL_0:
#return_stmt:
	iload 0
	iload 1
	imul
	ireturn
LABEL_1:


.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 5
	.limit locals 9

LABEL_0:

.var 1 is a D from LABEL_0 to LABEL_1
	ldc 0
	i2d
	dstore 1

.var 3 is b I from LABEL_0 to LABEL_1
	ldc 0
	istore 3
#b_equation:
	ldc 25
	istore 3

.var 4 is c I from LABEL_0 to LABEL_1
	ldc 0
	istore 4
#c_equation:
	ldc 3
	istore 4
#a_equation:
#call_sum:
	iload 3
	ldc 5
	idiv
	i2d
	ldc 3
	i2d
	invokestatic Simpliest_program/sum(DD)D
	ldc 1
	i2d
	dadd
#call_mult:
	ldc 2
	ldc -1
	imul
	iload 4
	invokestatic Simpliest_program/mult(II)I
	ldc 6
	idiv
	i2d
	dadd
	ldc 2
	i2d
	dmul
	ldc 2
	ldc 5
	imul
	ldc 3
	ldc 1
	isub
	imul
	i2d
	dsub
	ldc 35
	ldc 7
	idiv
	i2d
	dsub
	ldc 2
	i2d
	invokestatic java/lang/Math/pow(DD)D
	dstore 1
		iload 4
		ldc 5

		if_icmpge LABEL_1x0
LABEL_0x0:

.var 5 is d D from LABEL_0x0 to LABEL_1x0
		ldc 0
		i2d
		dstore 5
#call_print:
		getstatic java/lang/System/out Ljava/io/PrintStream;
		dload 1
		invokevirtual java/io/PrintStream/println(D)V
LABEL_1x0:
		dload 1
		ldc2_w 80.0

		dcmpg
		ifle LABEL_1x1
LABEL_0x1:
#call_print:
		getstatic java/lang/System/out Ljava/io/PrintStream;
		iload 4
		invokevirtual java/io/PrintStream/println(I)V
		goto LABEL_1x1_else_end
LABEL_1x1:
LABEL_0x1_else_begin:
#call_print:
		getstatic java/lang/System/out Ljava/io/PrintStream;
		iload 3
		invokevirtual java/io/PrintStream/println(I)V
LABEL_1x1_else_end:

.var 5 is d D from LABEL_0 to LABEL_1
	ldc 0
	i2d
	dstore 5

.var 7 is e D from LABEL_0 to LABEL_1
	ldc 0
	i2d
	dstore 7

LABEL_0x2:
		iload 4
		ldc 0

		if_icmple LABEL_1x2
#call_print:
		getstatic java/lang/System/out Ljava/io/PrintStream;
		iload 4
		invokevirtual java/io/PrintStream/println(I)V
#c_equation:
		iload 4
		ldc 1
		isub
		istore 4
		goto LABEL_0x2
LABEL_1x2:

LABEL_1:

#return_stmt:
	return

.end method
