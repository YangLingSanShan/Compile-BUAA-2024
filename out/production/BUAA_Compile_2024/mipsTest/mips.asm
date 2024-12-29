.data
g_a: .word 0, 1, 2, 3, 4, 5, 6, 7, 0, 0
g_idx: .word 1
s_0: .asciiz " "
s_1: .asciiz "\n"



.text

# Jump to main Function
jal main
j end
f_func:
func_block_label_0:

# %v_0 = load i32, i32* @g_idx
la , $k0 , g_idx
lw , $k0 , 0($k0)
sw , $k0 , -4($sp)

# %v_1 = add i32 %v_0, 1
lw , $k0 , -4($sp)
li , $k1 , 1
addu , $k0 , $k0 , $k1
sw , $k0 , -8($sp)

# store i32 %v_1, i32* @g_idx
la , $k1 , g_idx
lw , $k0 , -8($sp)
sw , $k0 , 0($k1)

# %v_2 = load i32, i32* @g_idx
la , $k0 , g_idx
lw , $k0 , 0($k0)
sw , $k0 , -12($sp)

# ret i32 %v_2
lw , $v0 , -12($sp)
jr $ra
main:
main_block_label_0:

# %v_0 = call i32 @f_func()
sw , $sp , -4($sp)
sw , $ra , -8($sp)
addi , $sp , $sp , -8
jal f_func
lw , $ra , 0($sp)
lw , $sp , 4($sp)
sw , $v0 , -4($sp)

# %v_1 = call i32 @f_func()
sw , $sp , -8($sp)
sw , $ra , -12($sp)
addi , $sp , $sp , -12
jal f_func
lw , $ra , 0($sp)
lw , $sp , 4($sp)
sw , $v0 , -8($sp)

# %v_2 = getelementptr inbounds [10 x i32], [10 x i32]* @g_a, i32 0, i32 %v_1
la , $k0 , g_a
lw , $k1 , -8($sp)
sll , $k1 , $k1 , 2
addu , $k0 , $k1 , $k0
sw , $k0 , -12($sp)

# %v_3 = call i32 @f_func()
sw , $sp , -16($sp)
sw , $ra , -20($sp)
addi , $sp , $sp , -20
jal f_func
lw , $ra , 0($sp)
lw , $sp , 4($sp)
sw , $v0 , -16($sp)

# %v_5 = getelementptr inbounds [10 x i32], [10 x i32]* @g_a, i32 0, i32 %v_3
la , $k0 , g_a
lw , $k1 , -16($sp)
sll , $k1 , $k1 , 2
addu , $k0 , $k1 , $k0
sw , $k0 , -20($sp)

# %v_4 = load i32, i32* %v_5
lw , $k0 , -20($sp)
lw , $k0 , 0($k0)
sw , $k0 , -24($sp)

# store i32 %v_4, i32* %v_2
lw , $k1 , -12($sp)
lw , $k0 , -24($sp)
sw , $k0 , 0($k1)

# %v_7 = getelementptr inbounds [10 x i32], [10 x i32]* @g_a, i32 0, i32 2
la , $k0 , g_a
addi , $k0 , $k0 , 8
sw , $k0 , -28($sp)

# %v_6 = load i32, i32* %v_7
lw , $k0 , -28($sp)
lw , $k0 , 0($k0)
sw , $k0 , -32($sp)

# %v_9 = getelementptr inbounds [10 x i32], [10 x i32]* @g_a, i32 0, i32 3
la , $k0 , g_a
addi , $k0 , $k0 , 12
sw , $k0 , -36($sp)

# %v_8 = load i32, i32* %v_9
lw , $k0 , -36($sp)
lw , $k0 , 0($k0)
sw , $k0 , -40($sp)

# call void @putint(i32 %v_6)
lw , $a0 , -32($sp)
li , $v0 , 1
syscall

# call void @putstr(i8* getelementptr inbounds ([2 x i8], [2 x i8]* @s_0, i64 0, i64 0))
la , $a0 , s_0
li , $v0 , 4
syscall

# call void @putint(i32 %v_8)
lw , $a0 , -40($sp)
li , $v0 , 1
syscall

# call void @putstr(i8* getelementptr inbounds ([2 x i8], [2 x i8]* @s_1, i64 0, i64 0))
la , $a0 , s_1
li , $v0 , 4
syscall

# ret i32 0
li , $v0 , 0
jr $ra
end:
