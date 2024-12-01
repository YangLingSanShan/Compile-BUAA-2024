.data
g_N: .word 10
g_a: .word 0, 1, 2, 3, 4, 5, 6, 7, 8, 9
s_0: .asciiz ", "
s_1: .asciiz "\n"
s_2: .asciiz ", "
s_3: .asciiz ", "
s_4: .asciiz "\n"



.text

# Jump to main Function
jal main
j end
f_fib:
fib_block_label_0:

# %v_0 = alloca i32
addi , $k0 , $sp , -8
sw , $k0 , -12($sp)

# store i32 %a_0, i32* %v_0
lw , $k1 , -12($sp)
lw , $k0 , -4($sp)
sw , $k0 , 0($k1)

# %v_1 = load i32, i32* %v_0
lw , $k0 , -12($sp)
lw , $k0 , 0($k0)
sw , $k0 , -16($sp)

# %v_2 = icmp eq i32 %v_1, 1
lw , $k0 , -16($sp)
li , $k1 , 1
seq , $k0 , $k0 , $k1
sw , $k0 , -20($sp)

# br i1 %v_2, label %fib_block_label_1, label %fib_block_label_2
lw , $k0 , -20($sp)
bne , $k0 , $zero , fib_block_label_1
j fib_block_label_2
fib_block_label_1:

# ret i32 1
li , $v0 , 1
jr $ra

# br label %fib_block_label_2
j fib_block_label_2
fib_block_label_2:

# %v_3 = load i32, i32* %v_0
lw , $k0 , -12($sp)
lw , $k0 , 0($k0)
sw , $k0 , -24($sp)

# %v_4 = icmp eq i32 %v_3, 2
lw , $k0 , -24($sp)
li , $k1 , 2
seq , $k0 , $k0 , $k1
sw , $k0 , -28($sp)

# br i1 %v_4, label %fib_block_label_3, label %fib_block_label_4
lw , $k0 , -28($sp)
bne , $k0 , $zero , fib_block_label_3
j fib_block_label_4
fib_block_label_3:

# ret i32 2
li , $v0 , 2
jr $ra

# br label %fib_block_label_4
j fib_block_label_4
fib_block_label_4:

# %v_5 = load i32, i32* %v_0
lw , $k0 , -12($sp)
lw , $k0 , 0($k0)
sw , $k0 , -32($sp)

# %v_6 = sub i32 %v_5, 1
lw , $k0 , -32($sp)
li , $k1 , 1
subu , $k0 , $k0 , $k1
sw , $k0 , -36($sp)

# %v_7 = call i32 @f_fib(i32 %v_6)
sw , $sp , -40($sp)
sw , $ra , -44($sp)
lw , $k0 , -36($sp)
sw , $k0 , -48($sp)
addi , $sp , $sp , -44
jal f_fib
lw , $ra , 0($sp)
lw , $sp , 4($sp)
sw , $v0 , -40($sp)

# %v_8 = load i32, i32* %v_0
lw , $k0 , -12($sp)
lw , $k0 , 0($k0)
sw , $k0 , -44($sp)

# %v_9 = sub i32 %v_8, 2
lw , $k0 , -44($sp)
li , $k1 , 2
subu , $k0 , $k0 , $k1
sw , $k0 , -48($sp)

# %v_10 = call i32 @f_fib(i32 %v_9)
sw , $sp , -52($sp)
sw , $ra , -56($sp)
lw , $k0 , -48($sp)
sw , $k0 , -60($sp)
addi , $sp , $sp , -56
jal f_fib
lw , $ra , 0($sp)
lw , $sp , 4($sp)
sw , $v0 , -52($sp)

# %v_11 = add i32 %v_7, %v_10
lw , $k0 , -40($sp)
lw , $k1 , -52($sp)
addu , $k0 , $k0 , $k1
sw , $k0 , -56($sp)

# ret i32 %v_11
lw , $v0 , -56($sp)
jr $ra
main:
main_block_label_0:

# %v_0 = alloca i32
addi , $k0 , $sp , -4
sw , $k0 , -8($sp)

# store i32 2, i32* %v_0
lw , $k1 , -8($sp)
li , $k0 , 2
sw , $k0 , 0($k1)

# %v_1 = alloca i32
addi , $k0 , $sp , -12
sw , $k0 , -16($sp)

# store i32 5, i32* %v_1
lw , $k1 , -16($sp)
li , $k0 , 5
sw , $k0 , 0($k1)

# %v_2 = alloca i32
addi , $k0 , $sp , -20
sw , $k0 , -24($sp)

# store i32 1, i32* %v_2
lw , $k1 , -24($sp)
li , $k0 , 1
sw , $k0 , 0($k1)

# %v_3 = alloca i32
addi , $k0 , $sp , -28
sw , $k0 , -32($sp)

# store i32 2, i32* %v_3
lw , $k1 , -32($sp)
li , $k0 , 2
sw , $k0 , 0($k1)

# %v_4 = call i32 @getint()
li , $v0 , 5
syscall
sw , $v0 , -36($sp)

# store i32 %v_4, i32* %v_0
lw , $k1 , -8($sp)
lw , $k0 , -36($sp)
sw , $k0 , 0($k1)

# %v_5 = call i32 @getint()
li , $v0 , 5
syscall
sw , $v0 , -40($sp)

# store i32 %v_5, i32* %v_1
lw , $k1 , -16($sp)
lw , $k0 , -40($sp)
sw , $k0 , 0($k1)

# %v_6 = load i32, i32* %v_0
lw , $k0 , -8($sp)
lw , $k0 , 0($k0)
sw , $k0 , -44($sp)

# %v_7 = load i32, i32* %v_1
lw , $k0 , -16($sp)
lw , $k0 , 0($k0)
sw , $k0 , -48($sp)

# %v_8 = mul i32 %v_6, %v_7
lw , $k0 , -44($sp)
lw , $k1 , -48($sp)
mult , $k0 , $k1
mflo , $k0
sw , $k0 , -52($sp)

# %v_9 = sub i32 0, %v_8
li , $k0 , 0
lw , $k1 , -52($sp)
subu , $k0 , $k0 , $k1
sw , $k0 , -56($sp)

# %v_10 = call i32 @f_fib(i32 4)
sw , $sp , -60($sp)
sw , $ra , -64($sp)
li , $k0 , 4
sw , $k0 , -68($sp)
addi , $sp , $sp , -64
jal f_fib
lw , $ra , 0($sp)
lw , $sp , 4($sp)
sw , $v0 , -60($sp)

# %v_11 = mul i32 %v_9, %v_10
lw , $k0 , -56($sp)
lw , $k1 , -60($sp)
mult , $k0 , $k1
mflo , $k0
sw , $k0 , -64($sp)

# %v_12 = add i32 %v_11, 0
lw , $k0 , -64($sp)
li , $k1 , 0
addu , $k0 , $k0 , $k1
sw , $k0 , -68($sp)

# %v_14 = getelementptr inbounds [10 x i32], [10 x i32]* @g_a, i32 0, i32 1
la , $k0 , g_a
addi , $k0 , $k0 , 4
sw , $k0 , -72($sp)

# %v_13 = load i32, i32* %v_14
lw , $k0 , -72($sp)
lw , $k0 , 0($k0)
sw , $k0 , -76($sp)

# %v_15 = mul i32 %v_13, 1
lw , $k0 , -76($sp)
li , $k1 , 1
mult , $k0 , $k1
mflo , $k0
sw , $k0 , -80($sp)

# %v_16 = add i32 %v_12, %v_15
lw , $k0 , -68($sp)
lw , $k1 , -80($sp)
addu , $k0 , $k0 , $k1
sw , $k0 , -84($sp)

# %v_17 = sdiv i32 1, 2
li , $k0 , 1
li , $k1 , 2
div , $k0 , $k1
mflo , $k0
sw , $k0 , -88($sp)

# %v_18 = sub i32 %v_16, %v_17
lw , $k0 , -84($sp)
lw , $k1 , -88($sp)
subu , $k0 , $k0 , $k1
sw , $k0 , -92($sp)

# %v_19 = sdiv i32 %v_18, 5
lw , $k0 , -92($sp)
li , $k1 , 5
div , $k0 , $k1
mflo , $k0
sw , $k0 , -96($sp)

# store i32 %v_19, i32* %v_0
lw , $k1 , -8($sp)
lw , $k0 , -96($sp)
sw , $k0 , 0($k1)

# %v_20 = mul i32 7, 5923
li , $k0 , 7
li , $k1 , 5923
mult , $k0 , $k1
mflo , $k0
sw , $k0 , -100($sp)

# %v_21 = srem i32 %v_20, 56
lw , $k0 , -100($sp)
li , $k1 , 56
div , $k0 , $k1
mfhi , $k0
sw , $k0 , -104($sp)

# %v_22 = mul i32 %v_21, 57
lw , $k0 , -104($sp)
li , $k1 , 57
mult , $k0 , $k1
mflo , $k0
sw , $k0 , -108($sp)

# %v_23 = call i32 @f_fib(i32 5)
sw , $sp , -112($sp)
sw , $ra , -116($sp)
li , $k0 , 5
sw , $k0 , -120($sp)
addi , $sp , $sp , -116
jal f_fib
lw , $ra , 0($sp)
lw , $sp , 4($sp)
sw , $v0 , -112($sp)

# %v_24 = add i32 %v_23, 2
lw , $k0 , -112($sp)
li , $k1 , 2
addu , $k0 , $k0 , $k1
sw , $k0 , -116($sp)

# %v_25 = call i32 @f_fib(i32 %v_24)
sw , $sp , -120($sp)
sw , $ra , -124($sp)
lw , $k0 , -116($sp)
sw , $k0 , -128($sp)
addi , $sp , $sp , -124
jal f_fib
lw , $ra , 0($sp)
lw , $sp , 4($sp)
sw , $v0 , -120($sp)

# %v_26 = sub i32 %v_22, %v_25
lw , $k0 , -108($sp)
lw , $k1 , -120($sp)
subu , $k0 , $k0 , $k1
sw , $k0 , -124($sp)

# %v_27 = load i32, i32* %v_2
lw , $k0 , -24($sp)
lw , $k0 , 0($k0)
sw , $k0 , -128($sp)

# %v_28 = load i32, i32* %v_3
lw , $k0 , -32($sp)
lw , $k0 , 0($k0)
sw , $k0 , -132($sp)

# %v_29 = add i32 %v_27, %v_28
lw , $k0 , -128($sp)
lw , $k1 , -132($sp)
addu , $k0 , $k0 , $k1
sw , $k0 , -136($sp)

# %v_30 = sdiv i32 89, 2
li , $k0 , 89
li , $k1 , 2
div , $k0 , $k1
mflo , $k0
sw , $k0 , -140($sp)

# %v_31 = mul i32 %v_30, 36
lw , $k0 , -140($sp)
li , $k1 , 36
mult , $k0 , $k1
mflo , $k0
sw , $k0 , -144($sp)

# %v_32 = sub i32 %v_31, 53
lw , $k0 , -144($sp)
li , $k1 , 53
subu , $k0 , $k0 , $k1
sw , $k0 , -148($sp)

# %v_33 = sdiv i32 %v_32, 1
lw , $k0 , -148($sp)
li , $k1 , 1
div , $k0 , $k1
mflo , $k0
sw , $k0 , -152($sp)

# %v_34 = mul i32 %v_33, 6
lw , $k0 , -152($sp)
li , $k1 , 6
mult , $k0 , $k1
mflo , $k0
sw , $k0 , -156($sp)

# %v_35 = sub i32 %v_29, %v_34
lw , $k0 , -136($sp)
lw , $k1 , -156($sp)
subu , $k0 , $k0 , $k1
sw , $k0 , -160($sp)

# %v_36 = mul i32 45, 56
li , $k0 , 45
li , $k1 , 56
mult , $k0 , $k1
mflo , $k0
sw , $k0 , -164($sp)

# %v_37 = sdiv i32 %v_36, 85
lw , $k0 , -164($sp)
li , $k1 , 85
div , $k0 , $k1
mflo , $k0
sw , $k0 , -168($sp)

# %v_38 = sub i32 %v_37, 56
lw , $k0 , -168($sp)
li , $k1 , 56
subu , $k0 , $k0 , $k1
sw , $k0 , -172($sp)

# %v_39 = mul i32 35, 56
li , $k0 , 35
li , $k1 , 56
mult , $k0 , $k1
mflo , $k0
sw , $k0 , -176($sp)

# %v_40 = sdiv i32 %v_39, 4
lw , $k0 , -176($sp)
li , $k1 , 4
div , $k0 , $k1
mflo , $k0
sw , $k0 , -180($sp)

# %v_41 = add i32 %v_38, %v_40
lw , $k0 , -172($sp)
lw , $k1 , -180($sp)
addu , $k0 , $k0 , $k1
sw , $k0 , -184($sp)

# %v_42 = sub i32 %v_41, 9
lw , $k0 , -184($sp)
li , $k1 , 9
subu , $k0 , $k0 , $k1
sw , $k0 , -188($sp)

# %v_43 = mul i32 2, %v_42
li , $k0 , 2
lw , $k1 , -188($sp)
mult , $k0 , $k1
mflo , $k0
sw , $k0 , -192($sp)

# %v_44 = sub i32 %v_35, %v_43
lw , $k0 , -160($sp)
lw , $k1 , -192($sp)
subu , $k0 , $k0 , $k1
sw , $k0 , -196($sp)

# %v_45 = add i32 %v_26, %v_44
lw , $k0 , -124($sp)
lw , $k1 , -196($sp)
addu , $k0 , $k0 , $k1
sw , $k0 , -200($sp)

# store i32 %v_45, i32* %v_1
lw , $k1 , -16($sp)
lw , $k0 , -200($sp)
sw , $k0 , 0($k1)

# %v_46 = alloca i32
addi , $k0 , $sp , -204
sw , $k0 , -208($sp)

# %v_47 = sub i32 0, 6
li , $k0 , 0
li , $k1 , 6
subu , $k0 , $k0 , $k1
sw , $k0 , -212($sp)

# store i32 %v_47, i32* %v_46
lw , $k1 , -208($sp)
lw , $k0 , -212($sp)
sw , $k0 , 0($k1)

# %v_48 = getelementptr inbounds [10 x i32], [10 x i32]* @g_a, i32 0, i32 0
la , $k0 , g_a
addi , $k0 , $k0 , 0
sw , $k0 , -216($sp)

# %v_50 = getelementptr inbounds [10 x i32], [10 x i32]* @g_a, i32 0, i32 0
la , $k0 , g_a
addi , $k0 , $k0 , 0
sw , $k0 , -220($sp)

# %v_49 = load i32, i32* %v_50
lw , $k0 , -220($sp)
lw , $k0 , 0($k0)
sw , $k0 , -224($sp)

# %v_51 = load i32, i32* %v_0
lw , $k0 , -8($sp)
lw , $k0 , 0($k0)
sw , $k0 , -228($sp)

# %v_52 = load i32, i32* %v_0
lw , $k0 , -8($sp)
lw , $k0 , 0($k0)
sw , $k0 , -232($sp)

# %v_53 = mul i32 %v_51, %v_52
lw , $k0 , -228($sp)
lw , $k1 , -232($sp)
mult , $k0 , $k1
mflo , $k0
sw , $k0 , -236($sp)

# %v_54 = add i32 %v_49, %v_53
lw , $k0 , -224($sp)
lw , $k1 , -236($sp)
addu , $k0 , $k0 , $k1
sw , $k0 , -240($sp)

# store i32 %v_54, i32* %v_48
lw , $k1 , -216($sp)
lw , $k0 , -240($sp)
sw , $k0 , 0($k1)

# %v_55 = getelementptr inbounds [10 x i32], [10 x i32]* @g_a, i32 0, i32 1
la , $k0 , g_a
addi , $k0 , $k0 , 4
sw , $k0 , -244($sp)

# %v_57 = getelementptr inbounds [10 x i32], [10 x i32]* @g_a, i32 0, i32 1
la , $k0 , g_a
addi , $k0 , $k0 , 4
sw , $k0 , -248($sp)

# %v_56 = load i32, i32* %v_57
lw , $k0 , -248($sp)
lw , $k0 , 0($k0)
sw , $k0 , -252($sp)

# %v_58 = load i32, i32* %v_0
lw , $k0 , -8($sp)
lw , $k0 , 0($k0)
sw , $k0 , -256($sp)

# %v_59 = load i32, i32* %v_0
lw , $k0 , -8($sp)
lw , $k0 , 0($k0)
sw , $k0 , -260($sp)

# %v_60 = mul i32 %v_58, %v_59
lw , $k0 , -256($sp)
lw , $k1 , -260($sp)
mult , $k0 , $k1
mflo , $k0
sw , $k0 , -264($sp)

# %v_61 = add i32 %v_56, %v_60
lw , $k0 , -252($sp)
lw , $k1 , -264($sp)
addu , $k0 , $k0 , $k1
sw , $k0 , -268($sp)

# store i32 %v_61, i32* %v_55
lw , $k1 , -244($sp)
lw , $k0 , -268($sp)
sw , $k0 , 0($k1)

# %v_62 = getelementptr inbounds [10 x i32], [10 x i32]* @g_a, i32 0, i32 2
la , $k0 , g_a
addi , $k0 , $k0 , 8
sw , $k0 , -272($sp)

# %v_64 = getelementptr inbounds [10 x i32], [10 x i32]* @g_a, i32 0, i32 2
la , $k0 , g_a
addi , $k0 , $k0 , 8
sw , $k0 , -276($sp)

# %v_63 = load i32, i32* %v_64
lw , $k0 , -276($sp)
lw , $k0 , 0($k0)
sw , $k0 , -280($sp)

# %v_65 = load i32, i32* %v_0
lw , $k0 , -8($sp)
lw , $k0 , 0($k0)
sw , $k0 , -284($sp)

# %v_66 = load i32, i32* %v_0
lw , $k0 , -8($sp)
lw , $k0 , 0($k0)
sw , $k0 , -288($sp)

# %v_67 = mul i32 %v_65, %v_66
lw , $k0 , -284($sp)
lw , $k1 , -288($sp)
mult , $k0 , $k1
mflo , $k0
sw , $k0 , -292($sp)

# %v_68 = add i32 %v_63, %v_67
lw , $k0 , -280($sp)
lw , $k1 , -292($sp)
addu , $k0 , $k0 , $k1
sw , $k0 , -296($sp)

# store i32 %v_68, i32* %v_62
lw , $k1 , -272($sp)
lw , $k0 , -296($sp)
sw , $k0 , 0($k1)

# %v_69 = getelementptr inbounds [10 x i32], [10 x i32]* @g_a, i32 0, i32 3
la , $k0 , g_a
addi , $k0 , $k0 , 12
sw , $k0 , -300($sp)

# %v_71 = getelementptr inbounds [10 x i32], [10 x i32]* @g_a, i32 0, i32 3
la , $k0 , g_a
addi , $k0 , $k0 , 12
sw , $k0 , -304($sp)

# %v_70 = load i32, i32* %v_71
lw , $k0 , -304($sp)
lw , $k0 , 0($k0)
sw , $k0 , -308($sp)

# %v_72 = load i32, i32* %v_0
lw , $k0 , -8($sp)
lw , $k0 , 0($k0)
sw , $k0 , -312($sp)

# %v_73 = load i32, i32* %v_0
lw , $k0 , -8($sp)
lw , $k0 , 0($k0)
sw , $k0 , -316($sp)

# %v_74 = mul i32 %v_72, %v_73
lw , $k0 , -312($sp)
lw , $k1 , -316($sp)
mult , $k0 , $k1
mflo , $k0
sw , $k0 , -320($sp)

# %v_75 = add i32 %v_70, %v_74
lw , $k0 , -308($sp)
lw , $k1 , -320($sp)
addu , $k0 , $k0 , $k1
sw , $k0 , -324($sp)

# store i32 %v_75, i32* %v_69
lw , $k1 , -300($sp)
lw , $k0 , -324($sp)
sw , $k0 , 0($k1)

# %v_76 = getelementptr inbounds [10 x i32], [10 x i32]* @g_a, i32 0, i32 4
la , $k0 , g_a
addi , $k0 , $k0 , 16
sw , $k0 , -328($sp)

# %v_78 = getelementptr inbounds [10 x i32], [10 x i32]* @g_a, i32 0, i32 4
la , $k0 , g_a
addi , $k0 , $k0 , 16
sw , $k0 , -332($sp)

# %v_77 = load i32, i32* %v_78
lw , $k0 , -332($sp)
lw , $k0 , 0($k0)
sw , $k0 , -336($sp)

# %v_79 = load i32, i32* %v_0
lw , $k0 , -8($sp)
lw , $k0 , 0($k0)
sw , $k0 , -340($sp)

# %v_80 = load i32, i32* %v_0
lw , $k0 , -8($sp)
lw , $k0 , 0($k0)
sw , $k0 , -344($sp)

# %v_81 = mul i32 %v_79, %v_80
lw , $k0 , -340($sp)
lw , $k1 , -344($sp)
mult , $k0 , $k1
mflo , $k0
sw , $k0 , -348($sp)

# %v_82 = add i32 %v_77, %v_81
lw , $k0 , -336($sp)
lw , $k1 , -348($sp)
addu , $k0 , $k0 , $k1
sw , $k0 , -352($sp)

# store i32 %v_82, i32* %v_76
lw , $k1 , -328($sp)
lw , $k0 , -352($sp)
sw , $k0 , 0($k1)

# %v_83 = getelementptr inbounds [10 x i32], [10 x i32]* @g_a, i32 0, i32 5
la , $k0 , g_a
addi , $k0 , $k0 , 20
sw , $k0 , -356($sp)

# %v_85 = getelementptr inbounds [10 x i32], [10 x i32]* @g_a, i32 0, i32 5
la , $k0 , g_a
addi , $k0 , $k0 , 20
sw , $k0 , -360($sp)

# %v_84 = load i32, i32* %v_85
lw , $k0 , -360($sp)
lw , $k0 , 0($k0)
sw , $k0 , -364($sp)

# %v_86 = load i32, i32* %v_0
lw , $k0 , -8($sp)
lw , $k0 , 0($k0)
sw , $k0 , -368($sp)

# %v_87 = load i32, i32* %v_0
lw , $k0 , -8($sp)
lw , $k0 , 0($k0)
sw , $k0 , -372($sp)

# %v_88 = mul i32 %v_86, %v_87
lw , $k0 , -368($sp)
lw , $k1 , -372($sp)
mult , $k0 , $k1
mflo , $k0
sw , $k0 , -376($sp)

# %v_89 = add i32 %v_84, %v_88
lw , $k0 , -364($sp)
lw , $k1 , -376($sp)
addu , $k0 , $k0 , $k1
sw , $k0 , -380($sp)

# store i32 %v_89, i32* %v_83
lw , $k1 , -356($sp)
lw , $k0 , -380($sp)
sw , $k0 , 0($k1)

# %v_90 = getelementptr inbounds [10 x i32], [10 x i32]* @g_a, i32 0, i32 6
la , $k0 , g_a
addi , $k0 , $k0 , 24
sw , $k0 , -384($sp)

# %v_92 = getelementptr inbounds [10 x i32], [10 x i32]* @g_a, i32 0, i32 6
la , $k0 , g_a
addi , $k0 , $k0 , 24
sw , $k0 , -388($sp)

# %v_91 = load i32, i32* %v_92
lw , $k0 , -388($sp)
lw , $k0 , 0($k0)
sw , $k0 , -392($sp)

# %v_93 = load i32, i32* %v_0
lw , $k0 , -8($sp)
lw , $k0 , 0($k0)
sw , $k0 , -396($sp)

# %v_94 = load i32, i32* %v_0
lw , $k0 , -8($sp)
lw , $k0 , 0($k0)
sw , $k0 , -400($sp)

# %v_95 = mul i32 %v_93, %v_94
lw , $k0 , -396($sp)
lw , $k1 , -400($sp)
mult , $k0 , $k1
mflo , $k0
sw , $k0 , -404($sp)

# %v_96 = add i32 %v_91, %v_95
lw , $k0 , -392($sp)
lw , $k1 , -404($sp)
addu , $k0 , $k0 , $k1
sw , $k0 , -408($sp)

# store i32 %v_96, i32* %v_90
lw , $k1 , -384($sp)
lw , $k0 , -408($sp)
sw , $k0 , 0($k1)

# %v_97 = getelementptr inbounds [10 x i32], [10 x i32]* @g_a, i32 0, i32 7
la , $k0 , g_a
addi , $k0 , $k0 , 28
sw , $k0 , -412($sp)

# %v_99 = getelementptr inbounds [10 x i32], [10 x i32]* @g_a, i32 0, i32 7
la , $k0 , g_a
addi , $k0 , $k0 , 28
sw , $k0 , -416($sp)

# %v_98 = load i32, i32* %v_99
lw , $k0 , -416($sp)
lw , $k0 , 0($k0)
sw , $k0 , -420($sp)

# %v_100 = load i32, i32* %v_0
lw , $k0 , -8($sp)
lw , $k0 , 0($k0)
sw , $k0 , -424($sp)

# %v_101 = load i32, i32* %v_0
lw , $k0 , -8($sp)
lw , $k0 , 0($k0)
sw , $k0 , -428($sp)

# %v_102 = mul i32 %v_100, %v_101
lw , $k0 , -424($sp)
lw , $k1 , -428($sp)
mult , $k0 , $k1
mflo , $k0
sw , $k0 , -432($sp)

# %v_103 = add i32 %v_98, %v_102
lw , $k0 , -420($sp)
lw , $k1 , -432($sp)
addu , $k0 , $k0 , $k1
sw , $k0 , -436($sp)

# store i32 %v_103, i32* %v_97
lw , $k1 , -412($sp)
lw , $k0 , -436($sp)
sw , $k0 , 0($k1)

# %v_104 = getelementptr inbounds [10 x i32], [10 x i32]* @g_a, i32 0, i32 8
la , $k0 , g_a
addi , $k0 , $k0 , 32
sw , $k0 , -440($sp)

# %v_106 = getelementptr inbounds [10 x i32], [10 x i32]* @g_a, i32 0, i32 8
la , $k0 , g_a
addi , $k0 , $k0 , 32
sw , $k0 , -444($sp)

# %v_105 = load i32, i32* %v_106
lw , $k0 , -444($sp)
lw , $k0 , 0($k0)
sw , $k0 , -448($sp)

# %v_107 = load i32, i32* %v_0
lw , $k0 , -8($sp)
lw , $k0 , 0($k0)
sw , $k0 , -452($sp)

# %v_108 = load i32, i32* %v_0
lw , $k0 , -8($sp)
lw , $k0 , 0($k0)
sw , $k0 , -456($sp)

# %v_109 = mul i32 %v_107, %v_108
lw , $k0 , -452($sp)
lw , $k1 , -456($sp)
mult , $k0 , $k1
mflo , $k0
sw , $k0 , -460($sp)

# %v_110 = add i32 %v_105, %v_109
lw , $k0 , -448($sp)
lw , $k1 , -460($sp)
addu , $k0 , $k0 , $k1
sw , $k0 , -464($sp)

# store i32 %v_110, i32* %v_104
lw , $k1 , -440($sp)
lw , $k0 , -464($sp)
sw , $k0 , 0($k1)

# %v_111 = getelementptr inbounds [10 x i32], [10 x i32]* @g_a, i32 0, i32 9
la , $k0 , g_a
addi , $k0 , $k0 , 36
sw , $k0 , -468($sp)

# %v_113 = getelementptr inbounds [10 x i32], [10 x i32]* @g_a, i32 0, i32 9
la , $k0 , g_a
addi , $k0 , $k0 , 36
sw , $k0 , -472($sp)

# %v_112 = load i32, i32* %v_113
lw , $k0 , -472($sp)
lw , $k0 , 0($k0)
sw , $k0 , -476($sp)

# %v_114 = load i32, i32* %v_0
lw , $k0 , -8($sp)
lw , $k0 , 0($k0)
sw , $k0 , -480($sp)

# %v_115 = load i32, i32* %v_0
lw , $k0 , -8($sp)
lw , $k0 , 0($k0)
sw , $k0 , -484($sp)

# %v_116 = mul i32 %v_114, %v_115
lw , $k0 , -480($sp)
lw , $k1 , -484($sp)
mult , $k0 , $k1
mflo , $k0
sw , $k0 , -488($sp)

# %v_117 = add i32 %v_112, %v_116
lw , $k0 , -476($sp)
lw , $k1 , -488($sp)
addu , $k0 , $k0 , $k1
sw , $k0 , -492($sp)

# store i32 %v_117, i32* %v_111
lw , $k1 , -468($sp)
lw , $k0 , -492($sp)
sw , $k0 , 0($k1)

# store i32 0, i32* %v_0
lw , $k1 , -8($sp)
li , $k0 , 0
sw , $k0 , 0($k1)

# br label %main_block_label_1
j main_block_label_1
main_block_label_1:

# %v_118 = load i32, i32* %v_0
lw , $k0 , -8($sp)
lw , $k0 , 0($k0)
sw , $k0 , -496($sp)

# %v_119 = icmp slt i32 %v_118, 10
lw , $k0 , -496($sp)
li , $k1 , 10
slt , $k0 , $k0 , $k1
sw , $k0 , -500($sp)

# br i1 %v_119, label %main_block_label_2, label %main_block_label_3
lw , $k0 , -500($sp)
bne , $k0 , $zero , main_block_label_2
j main_block_label_3
main_block_label_2:

# %v_120 = load i32, i32* %v_0
lw , $k0 , -8($sp)
lw , $k0 , 0($k0)
sw , $k0 , -504($sp)

# %v_122 = getelementptr inbounds [10 x i32], [10 x i32]* @g_a, i32 0, i32 %v_120
la , $k0 , g_a
lw , $k1 , -504($sp)
sll , $k1 , $k1 , 2
addu , $k0 , $k1 , $k0
sw , $k0 , -508($sp)

# %v_121 = load i32, i32* %v_122
lw , $k0 , -508($sp)
lw , $k0 , 0($k0)
sw , $k0 , -512($sp)

# call void @putint(i32 %v_121)
lw , $a0 , -512($sp)
li , $v0 , 1
syscall

# call void @putstr(i8* getelementptr inbounds ([3 x i8], [3 x i8]* @s_0, i64 0, i64 0))
la , $a0 , s_0
li , $v0 , 4
syscall

# %v_123 = load i32, i32* %v_0
lw , $k0 , -8($sp)
lw , $k0 , 0($k0)
sw , $k0 , -516($sp)

# %v_124 = add i32 %v_123, 1
lw , $k0 , -516($sp)
li , $k1 , 1
addu , $k0 , $k0 , $k1
sw , $k0 , -520($sp)

# store i32 %v_124, i32* %v_0
lw , $k1 , -8($sp)
lw , $k0 , -520($sp)
sw , $k0 , 0($k1)

# br label %main_block_label_1
j main_block_label_1
main_block_label_3:

# %v_125 = load i32, i32* %v_0
lw , $k0 , -8($sp)
lw , $k0 , 0($k0)
sw , $k0 , -524($sp)

# %v_126 = load i32, i32* %v_1
lw , $k0 , -16($sp)
lw , $k0 , 0($k0)
sw , $k0 , -528($sp)

# %v_127 = load i32, i32* %v_46
lw , $k0 , -208($sp)
lw , $k0 , 0($k0)
sw , $k0 , -532($sp)

# call void @putstr(i8* getelementptr inbounds ([2 x i8], [2 x i8]* @s_1, i64 0, i64 0))
la , $a0 , s_1
li , $v0 , 4
syscall

# call void @putint(i32 %v_125)
lw , $a0 , -524($sp)
li , $v0 , 1
syscall

# call void @putstr(i8* getelementptr inbounds ([3 x i8], [3 x i8]* @s_2, i64 0, i64 0))
la , $a0 , s_2
li , $v0 , 4
syscall

# call void @putint(i32 %v_126)
lw , $a0 , -528($sp)
li , $v0 , 1
syscall

# call void @putstr(i8* getelementptr inbounds ([3 x i8], [3 x i8]* @s_3, i64 0, i64 0))
la , $a0 , s_3
li , $v0 , 4
syscall

# call void @putint(i32 %v_127)
lw , $a0 , -532($sp)
li , $v0 , 1
syscall

# call void @putstr(i8* getelementptr inbounds ([2 x i8], [2 x i8]* @s_4, i64 0, i64 0))
la , $a0 , s_4
li , $v0 , 4
syscall

# ret i32 0
li , $v0 , 0
jr $ra
end: