declare i32 @getint() 
declare i32 @getchar() 
declare void @putint(i32)
declare void @putch(i32)
declare void @putstr(i8*)


@g_a = dso_local global [5 x i8] [i8 49, i8 50, i8 51, i8 52, i8 53]

define dso_local i32 @main() {

main_block_label_0:
	%v_0 = getelementptr inbounds [5 x i8], [5 x i8]* @g_a, i32 0, i32 2
	store i8 99, i8* %v_0
	ret i32 0
}

