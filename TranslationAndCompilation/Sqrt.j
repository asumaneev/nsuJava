.class public Sqrt
.super java/lang/Object 

.method public <init>()V
	aload_0
	invokespecial java/lang/Object/<init>()V
	return
.end method

.method public static sqrt(I)I
	.limit stack 4
	.limit locals 3
.var 0 is n I from LABEL_0 to LABEL_1

LABEL_0:

.var 1 is y I from LABEL_0 to LABEL_1
	ldc 0
	istore 1

.var 2 is x I from LABEL_0 to LABEL_1
	ldc 0
	istore 2

LABEL_0x0:
		iload 1
		iload 0

		if_icmpge LABEL_1x0
#y_equation:
		iload 1
		ldc 2
		iload 2
		imul
		iadd
		ldc 1
		iadd
		istore 1
#x_equation:
		iload 2
		ldc 1
		iadd
		istore 2
		goto LABEL_0x0
LABEL_1x0:

		iload 1
		iload 0

		if_icmpne LABEL_1x1
LABEL_0x1:
#return_stmt:
		iload 2
		ireturn
		goto LABEL_1x1_else_end
LABEL_1x1:
LABEL_0x1_else_begin:
#return_stmt:
		iload 2
		ldc 1
		isub
		ireturn
LABEL_1x1_else_end:
LABEL_1:

	return

.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 10
	.limit locals 1

LABEL_0:
#call_print:
	getstatic java/lang/System/out Ljava/io/PrintStream;
#call_sqrt:
	ldc 25
	invokestatic Sqrt/sqrt(I)I
	invokevirtual java/io/PrintStream/println(I)V
#call_print:
	getstatic java/lang/System/out Ljava/io/PrintStream;
#call_sqrt:
	ldc 26
	invokestatic Sqrt/sqrt(I)I
	invokevirtual java/io/PrintStream/println(I)V
#call_print:
	getstatic java/lang/System/out Ljava/io/PrintStream;
#call_sqrt:
	ldc 27
	invokestatic Sqrt/sqrt(I)I
	invokevirtual java/io/PrintStream/println(I)V
#call_print:
	getstatic java/lang/System/out Ljava/io/PrintStream;
#call_sqrt:
	ldc 28
	invokestatic Sqrt/sqrt(I)I
	invokevirtual java/io/PrintStream/println(I)V
#call_print:
	getstatic java/lang/System/out Ljava/io/PrintStream;
#call_sqrt:
	ldc 29
	invokestatic Sqrt/sqrt(I)I
	invokevirtual java/io/PrintStream/println(I)V
#call_print:
	getstatic java/lang/System/out Ljava/io/PrintStream;
#call_sqrt:
	ldc 30
	invokestatic Sqrt/sqrt(I)I
	invokevirtual java/io/PrintStream/println(I)V
#call_print:
	getstatic java/lang/System/out Ljava/io/PrintStream;
#call_sqrt:
	ldc 31
	invokestatic Sqrt/sqrt(I)I
	invokevirtual java/io/PrintStream/println(I)V
#call_print:
	getstatic java/lang/System/out Ljava/io/PrintStream;
#call_sqrt:
	ldc 32
	invokestatic Sqrt/sqrt(I)I
	invokevirtual java/io/PrintStream/println(I)V
#call_print:
	getstatic java/lang/System/out Ljava/io/PrintStream;
#call_sqrt:
	ldc 33
	invokestatic Sqrt/sqrt(I)I
	invokevirtual java/io/PrintStream/println(I)V
#call_print:
	getstatic java/lang/System/out Ljava/io/PrintStream;
#call_sqrt:
	ldc 34
	invokestatic Sqrt/sqrt(I)I
	invokevirtual java/io/PrintStream/println(I)V
#call_print:
	getstatic java/lang/System/out Ljava/io/PrintStream;
#call_sqrt:
	ldc 35
	invokestatic Sqrt/sqrt(I)I
	invokevirtual java/io/PrintStream/println(I)V
#call_print:
	getstatic java/lang/System/out Ljava/io/PrintStream;
#call_sqrt:
	ldc 36
	invokestatic Sqrt/sqrt(I)I
	invokevirtual java/io/PrintStream/println(I)V
LABEL_1:

#return_stmt:
	return

.end method
