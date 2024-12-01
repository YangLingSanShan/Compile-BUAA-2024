; ModuleID = 'llvm-link'
source_filename = "llvm-link"
target datalayout = "e-m:w-p270:32:32-p271:32:32-p272:64:64-i64:64-i128:128-f80:128-n8:16:32:64-S128"
target triple = "x86_64-w64-windows-gnu"

@N = dso_local constant i32 10, align 4
@a = dso_local global [10 x i32] [i32 0, i32 1, i32 2, i32 3, i32 4, i32 5, i32 6, i32 7, i32 8, i32 9], align 16
@.str = private unnamed_addr constant [5 x i8] c"%d, \00", align 1
@.str.1 = private unnamed_addr constant [13 x i8] c"\0A%d, %d, %d\0A\00", align 1
@.str.2 = private unnamed_addr constant [3 x i8] c"%c\00", align 1
@.str.1.5 = private unnamed_addr constant [3 x i8] c"%d\00", align 1
@.str.2.6 = private unnamed_addr constant [4 x i8] c"%d:\00", align 1
@.str.3 = private unnamed_addr constant [4 x i8] c" %d\00", align 1
@.str.4 = private unnamed_addr constant [2 x i8] c"\0A\00", align 1
@.str.5 = private unnamed_addr constant [3 x i8] c"%s\00", align 1

; Function Attrs: noinline nounwind optnone uwtable
define dso_local i32 @fib(i32 noundef %0) #0 {
  %2 = alloca i32, align 4
  %3 = alloca i32, align 4
  store i32 %0, ptr %3, align 4
  %4 = load i32, ptr %3, align 4
  %5 = icmp eq i32 %4, 1
  br i1 %5, label %6, label %7

6:                                                ; preds = %1
  store i32 1, ptr %2, align 4
  br label %19

7:                                                ; preds = %1
  %8 = load i32, ptr %3, align 4
  %9 = icmp eq i32 %8, 2
  br i1 %9, label %10, label %11

10:                                               ; preds = %7
  store i32 2, ptr %2, align 4
  br label %19

11:                                               ; preds = %7
  %12 = load i32, ptr %3, align 4
  %13 = sub nsw i32 %12, 1
  %14 = call i32 @fib(i32 noundef %13)
  %15 = load i32, ptr %3, align 4
  %16 = sub nsw i32 %15, 2
  %17 = call i32 @fib(i32 noundef %16)
  %18 = add nsw i32 %14, %17
  store i32 %18, ptr %2, align 4
  br label %19

19:                                               ; preds = %11, %10, %6
  %20 = load i32, ptr %2, align 4
  ret i32 %20
}

; Function Attrs: noinline nounwind optnone uwtable
define dso_local i32 @main() #0 {
  %1 = alloca i32, align 4
  %2 = alloca i32, align 4
  %3 = alloca i32, align 4
  %4 = alloca i32, align 4
  %5 = alloca i32, align 4
  %6 = alloca i32, align 4
  store i32 0, ptr %1, align 4
  store i32 2, ptr %2, align 4
  store i32 5, ptr %3, align 4
  store i32 1, ptr %4, align 4
  store i32 2, ptr %5, align 4
  %7 = call i32 @getint()
  store i32 %7, ptr %2, align 4
  %8 = call i32 @getint()
  store i32 %8, ptr %3, align 4
  %9 = load i32, ptr %2, align 4
  %10 = load i32, ptr %3, align 4
  %11 = mul nsw i32 %9, %10
  %12 = sub nsw i32 0, %11
  %13 = call i32 @fib(i32 noundef 4)
  %14 = mul nsw i32 %12, %13
  %15 = add nsw i32 %14, 0
  %16 = load i32, ptr getelementptr inbounds ([10 x i32], ptr @a, i64 0, i64 1), align 4
  %17 = mul nsw i32 %16, 1
  %18 = add nsw i32 %15, %17
  %19 = sub nsw i32 %18, 0
  %20 = sdiv i32 %19, 5
  store i32 %20, ptr %2, align 4
  %21 = call i32 @fib(i32 noundef 5)
  %22 = add nsw i32 %21, 2
  %23 = call i32 @fib(i32 noundef %22)
  %24 = sub nsw i32 1197, %23
  %25 = add nsw i32 %24, -10091
  store i32 %25, ptr %3, align 4
  store i32 -6, ptr %6, align 4
  %26 = load i32, ptr @a, align 16
  %27 = load i32, ptr %2, align 4
  %28 = load i32, ptr %2, align 4
  %29 = mul nsw i32 %27, %28
  %30 = add nsw i32 %26, %29
  store i32 %30, ptr @a, align 16
  %31 = load i32, ptr getelementptr inbounds ([10 x i32], ptr @a, i64 0, i64 1), align 4
  %32 = load i32, ptr %2, align 4
  %33 = load i32, ptr %2, align 4
  %34 = mul nsw i32 %32, %33
  %35 = add nsw i32 %31, %34
  store i32 %35, ptr getelementptr inbounds ([10 x i32], ptr @a, i64 0, i64 1), align 4
  %36 = load i32, ptr getelementptr inbounds ([10 x i32], ptr @a, i64 0, i64 2), align 8
  %37 = load i32, ptr %2, align 4
  %38 = load i32, ptr %2, align 4
  %39 = mul nsw i32 %37, %38
  %40 = add nsw i32 %36, %39
  store i32 %40, ptr getelementptr inbounds ([10 x i32], ptr @a, i64 0, i64 2), align 8
  %41 = load i32, ptr getelementptr inbounds ([10 x i32], ptr @a, i64 0, i64 3), align 4
  %42 = load i32, ptr %2, align 4
  %43 = load i32, ptr %2, align 4
  %44 = mul nsw i32 %42, %43
  %45 = add nsw i32 %41, %44
  store i32 %45, ptr getelementptr inbounds ([10 x i32], ptr @a, i64 0, i64 3), align 4
  %46 = load i32, ptr getelementptr inbounds ([10 x i32], ptr @a, i64 0, i64 4), align 16
  %47 = load i32, ptr %2, align 4
  %48 = load i32, ptr %2, align 4
  %49 = mul nsw i32 %47, %48
  %50 = add nsw i32 %46, %49
  store i32 %50, ptr getelementptr inbounds ([10 x i32], ptr @a, i64 0, i64 4), align 16
  %51 = load i32, ptr getelementptr inbounds ([10 x i32], ptr @a, i64 0, i64 5), align 4
  %52 = load i32, ptr %2, align 4
  %53 = load i32, ptr %2, align 4
  %54 = mul nsw i32 %52, %53
  %55 = add nsw i32 %51, %54
  store i32 %55, ptr getelementptr inbounds ([10 x i32], ptr @a, i64 0, i64 5), align 4
  %56 = load i32, ptr getelementptr inbounds ([10 x i32], ptr @a, i64 0, i64 6), align 8
  %57 = load i32, ptr %2, align 4
  %58 = load i32, ptr %2, align 4
  %59 = mul nsw i32 %57, %58
  %60 = add nsw i32 %56, %59
  store i32 %60, ptr getelementptr inbounds ([10 x i32], ptr @a, i64 0, i64 6), align 8
  %61 = load i32, ptr getelementptr inbounds ([10 x i32], ptr @a, i64 0, i64 7), align 4
  %62 = load i32, ptr %2, align 4
  %63 = load i32, ptr %2, align 4
  %64 = mul nsw i32 %62, %63
  %65 = add nsw i32 %61, %64
  store i32 %65, ptr getelementptr inbounds ([10 x i32], ptr @a, i64 0, i64 7), align 4
  %66 = load i32, ptr getelementptr inbounds ([10 x i32], ptr @a, i64 0, i64 8), align 16
  %67 = load i32, ptr %2, align 4
  %68 = load i32, ptr %2, align 4
  %69 = mul nsw i32 %67, %68
  %70 = add nsw i32 %66, %69
  store i32 %70, ptr getelementptr inbounds ([10 x i32], ptr @a, i64 0, i64 8), align 16
  %71 = load i32, ptr getelementptr inbounds ([10 x i32], ptr @a, i64 0, i64 9), align 4
  %72 = load i32, ptr %2, align 4
  %73 = load i32, ptr %2, align 4
  %74 = mul nsw i32 %72, %73
  %75 = add nsw i32 %71, %74
  store i32 %75, ptr getelementptr inbounds ([10 x i32], ptr @a, i64 0, i64 9), align 4
  store i32 0, ptr %2, align 4
  br label %76

76:                                               ; preds = %79, %0
  %77 = load i32, ptr %2, align 4
  %78 = icmp slt i32 %77, 10
  br i1 %78, label %79, label %87

79:                                               ; preds = %76
  %80 = load i32, ptr %2, align 4
  %81 = sext i32 %80 to i64
  %82 = getelementptr inbounds [10 x i32], ptr @a, i64 0, i64 %81
  %83 = load i32, ptr %82, align 4
  %84 = call i32 (ptr, ...) @printf(ptr noundef @.str, i32 noundef %83)
  %85 = load i32, ptr %2, align 4
  %86 = add nsw i32 %85, 1
  store i32 %86, ptr %2, align 4
  br label %76, !llvm.loop !5

87:                                               ; preds = %76
  %88 = load i32, ptr %2, align 4
  %89 = load i32, ptr %3, align 4
  %90 = load i32, ptr %6, align 4
  %91 = call i32 (ptr, ...) @printf(ptr noundef @.str.1, i32 noundef %88, i32 noundef %89, i32 noundef %90)
  ret i32 0
}

declare dso_local i32 @printf(ptr noundef, ...) #1

; Function Attrs: noinline nounwind optnone uwtable
define dso_local i32 @getchar() #0 {
  %1 = alloca i8, align 1
  %2 = call i32 (ptr, ...) @scanf(ptr noundef @.str.2, ptr noundef %1)
  %3 = load i8, ptr %1, align 1
  %4 = sext i8 %3 to i32
  ret i32 %4
}

declare dso_local i32 @scanf(ptr noundef, ...) #1

; Function Attrs: noinline nounwind optnone uwtable
define dso_local i32 @getint() #0 {
  %1 = alloca i32, align 4
  %2 = call i32 (ptr, ...) @scanf(ptr noundef @.str.1.5, ptr noundef %1)
  br label %3

3:                                                ; preds = %6, %0
  %4 = call i32 @getchar()
  %5 = icmp ne i32 %4, 10
  br i1 %5, label %6, label %7

6:                                                ; preds = %3
  br label %3, !llvm.loop !7

7:                                                ; preds = %3
  %8 = load i32, ptr %1, align 4
  ret i32 %8
}

; Function Attrs: noinline nounwind optnone uwtable
define dso_local i32 @getarray(ptr noundef %0) #0 {
  %2 = alloca ptr, align 8
  %3 = alloca i32, align 4
  %4 = alloca i32, align 4
  store ptr %0, ptr %2, align 8
  %5 = call i32 (ptr, ...) @scanf(ptr noundef @.str.1.5, ptr noundef %3)
  store i32 0, ptr %4, align 4
  br label %6

6:                                                ; preds = %16, %1
  %7 = load i32, ptr %4, align 4
  %8 = load i32, ptr %3, align 4
  %9 = icmp slt i32 %7, %8
  br i1 %9, label %10, label %19

10:                                               ; preds = %6
  %11 = load ptr, ptr %2, align 8
  %12 = load i32, ptr %4, align 4
  %13 = sext i32 %12 to i64
  %14 = getelementptr inbounds i32, ptr %11, i64 %13
  %15 = call i32 (ptr, ...) @scanf(ptr noundef @.str.1.5, ptr noundef %14)
  br label %16

16:                                               ; preds = %10
  %17 = load i32, ptr %4, align 4
  %18 = add nsw i32 %17, 1
  store i32 %18, ptr %4, align 4
  br label %6, !llvm.loop !8

19:                                               ; preds = %6
  %20 = load i32, ptr %3, align 4
  ret i32 %20
}

; Function Attrs: noinline nounwind optnone uwtable
define dso_local void @putint(i32 noundef %0) #0 {
  %2 = alloca i32, align 4
  store i32 %0, ptr %2, align 4
  %3 = load i32, ptr %2, align 4
  %4 = call i32 (ptr, ...) @printf(ptr noundef @.str.1.5, i32 noundef %3)
  ret void
}

; Function Attrs: noinline nounwind optnone uwtable
define dso_local void @putch(i32 noundef %0) #0 {
  %2 = alloca i32, align 4
  store i32 %0, ptr %2, align 4
  %3 = load i32, ptr %2, align 4
  %4 = call i32 (ptr, ...) @printf(ptr noundef @.str.2, i32 noundef %3)
  ret void
}

; Function Attrs: noinline nounwind optnone uwtable
define dso_local void @putarray(i32 noundef %0, ptr noundef %1) #0 {
  %3 = alloca i32, align 4
  %4 = alloca ptr, align 8
  %5 = alloca i32, align 4
  store i32 %0, ptr %3, align 4
  store ptr %1, ptr %4, align 8
  %6 = load i32, ptr %3, align 4
  %7 = call i32 (ptr, ...) @printf(ptr noundef @.str.2.6, i32 noundef %6)
  store i32 0, ptr %5, align 4
  br label %8

8:                                                ; preds = %19, %2
  %9 = load i32, ptr %5, align 4
  %10 = load i32, ptr %3, align 4
  %11 = icmp slt i32 %9, %10
  br i1 %11, label %12, label %22

12:                                               ; preds = %8
  %13 = load ptr, ptr %4, align 8
  %14 = load i32, ptr %5, align 4
  %15 = sext i32 %14 to i64
  %16 = getelementptr inbounds i32, ptr %13, i64 %15
  %17 = load i32, ptr %16, align 4
  %18 = call i32 (ptr, ...) @printf(ptr noundef @.str.3, i32 noundef %17)
  br label %19

19:                                               ; preds = %12
  %20 = load i32, ptr %5, align 4
  %21 = add nsw i32 %20, 1
  store i32 %21, ptr %5, align 4
  br label %8, !llvm.loop !9

22:                                               ; preds = %8
  %23 = call i32 (ptr, ...) @printf(ptr noundef @.str.4)
  ret void
}

; Function Attrs: noinline nounwind optnone uwtable
define dso_local void @putstr(ptr noundef %0) #0 {
  %2 = alloca ptr, align 8
  store ptr %0, ptr %2, align 8
  %3 = load ptr, ptr %2, align 8
  %4 = call i32 (ptr, ...) @printf(ptr noundef @.str.5, ptr noundef %3)
  ret void
}

attributes #0 = { noinline nounwind optnone uwtable "min-legal-vector-width"="0" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cmov,+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "tune-cpu"="generic" }
attributes #1 = { "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cmov,+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "tune-cpu"="generic" }

!llvm.ident = !{!0, !0}
!llvm.module.flags = !{!1, !2, !3, !4}

!0 = !{!"(built by Brecht Sanders, r8) clang version 18.1.5"}
!1 = !{i32 1, !"wchar_size", i32 2}
!2 = !{i32 8, !"PIC Level", i32 2}
!3 = !{i32 7, !"uwtable", i32 2}
!4 = !{i32 1, !"MaxTLSAlign", i32 65536}
!5 = distinct !{!5, !6}
!6 = !{!"llvm.loop.mustprogress"}
!7 = distinct !{!7, !6}
!8 = distinct !{!8, !6}
!9 = distinct !{!9, !6}
