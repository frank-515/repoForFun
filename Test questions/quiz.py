import random
import pandas as np
testLib = np.read_excel(io=r'quizLib.xls',header=0,usecols="A:I")
ans = "null"
maxQuiz = 4457
marks = 0
quiz = []

#print(testLib.loc[ 0, 'A':'D' ])
#print(testLib.columns)
def stars():
    print("*****************************************")

def outputQuiz( index ):
    print(testLib.loc[ index , '题目' ] + '\n')
    print(testLib.loc[ index , 'A' : 'D' ] )

def correctAns( index , ans ):
    if (ans.upper() == testLib.loc[ index , '答案' ]):
        return True
    else:
        return False

def randomQuiz( amount = 30 , maxQuiz = 4457 ):
    marks = 0
    for i in range(amount):
        quiz.append(random.randint( 0 , maxQuiz))
        quiz.sort()
    for i in quiz:
        outputQuiz( i )
        ans = str(input("Ans:"))
        if (correctAns( i , ans ) == True):
            marks += 1
            print("\033[32mBingo \033[0m" )
            stars()
        elif (correctAns( i , ans )) == False:
            print("\033[31mWrong！ \033[0m")
            print("The correct anser is " + testLib.loc[ i , '答案'])
            stars()
    return marks
amount = int(input("题目数量：\n"))
marks = randomQuiz( amount )
print("最终得分：" + str(marks) + "/" + str(amount))
