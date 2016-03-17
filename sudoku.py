import tkinter

presudoku=[]
anssudoku=[]
outputlabel=0

block=[[1,1,1,2,2,2,3,3,3],
       [1,1,1,2,2,2,3,3,3],
       [1,1,1,2,2,2,3,3,3],
       [4,4,4,5,5,5,6,6,6],
       [4,4,4,5,5,5,6,6,6],
       [4,4,4,5,5,5,6,6,6],
       [7,7,7,8,8,8,0,0,0],
       [7,7,7,8,8,8,0,0,0],
       [7,7,7,8,8,8,0,0,0]]

def getnums():
    global presudoku
    
    given=[]
    nums=[]
    for i in range(9):
        tmp1=[]
        tmp2=[]
        for j in range(9):
            try:
                ts=presudoku[i][j].get().strip()
                if ts=='':
                    tmp1.append(False)
                    tmp2.append(0)
                else:
                    ti=int(ts)
                    if ti<1 or ti>9:
                        raise ValueError("Out of (1-9) in row%d column%d"%(i+1,j+1))
                    tmp1.append(True)
                    tmp2.append(ti-1)
            except:
                tmp1.append(False)
                tmp2.append(0)
        given.append(tmp1)
        nums.append(tmp2)

    return [given,nums]

def fillin(given,nums,x,y,br,bc,bb):
    if y==9:
        y=0
        x=x+1
    if x==9:
        return True
    if given[x][y]:
        return fillin(given,nums,x,y+1,br,bc,bb)
    for i in range(9):
        if (not br[x][i]) and (not bc[y][i]) and (not bb[block[x][y]][i]):
            nums[x][y]=i
            br[x][i]=True
            bc[y][i]=True
            bb[block[x][y]][i]=True
            if fillin(given,nums,x,y+1,br,bc,bb):
                return True
            nums[x][y]=0
            br[x][i]=False
            bc[y][i]=False
            bb[block[x][y]][i]=False
    return False

def precalc(given,nums,br,bc,bb):
    global block
    
    legal=True
    for i in range(9):
        for j in range(9):
            if given[i][j]:
                if br[i][nums[i][j]]:
                    legal=False
                else:
                    br[i][nums[i][j]]=True
                if bc[j][nums[i][j]]:
                    legal=False
                else:
                    bc[j][nums[i][j]]=True
                if bb[block[i][j]][nums[i][j]]:
                    legal=False
                else:
                    bb[block[i][j]][nums[i][j]]=True

    return legal

def paint(nums,given):
    global anssudoku

    for i in range(9):
        for j in range(9):
            anssudoku[i][j]["text"]=nums[i][j]+1
            if given[i][j]:
                anssudoku[i][j]["bg"]="#FFF0FF"
            else:
                anssudoku[i][j]["bg"]="#FFFFF0"
            

def run():
    global presudoku
    global anssudoku
    global outputlabel
    
    given,nums=getnums()

    br=[ [False for j in range(9)]   for i in range(9)]
    bc=[ [False for j in range(9)]   for i in range(9)]
    bb=[ [False for j in range(9)]   for i in range(9)]

    if precalc(given,nums,br,bc,bb):
        outputlabel["text"]="rng"
        if fillin(given,nums,0,0,br,bc,bb):
            paint(nums,given)
        else:
            for i in range(len("NO ANS!")):
                anssudoku[0][i]["text"]="NO ANS!"[i]
        outputlabel["text"]="ok!"
    else:
        clc()
        for i in range(8):
            anssudoku[0][i]["text"]='ILLEGAL!'[i]

def clc():
    global anssudoku

    for i in range(9):
        for j in range(9):
            anssudoku[i][j]["text"]=""
            anssudoku[i][j]["bg"]="#F0F0F0"

def sudoku():
    global presudoku
    global anssudoku
    global outputlabel
    
    root=tkinter.Tk()
    root.wm_title("sudoku")

    presudoku=[[tkinter.Entry(master=root,width=3) for j in range(9)] for i in range(9)]
    anssudoku=[[tkinter.Label(master=root,width=3) for j in range(9)] for i in range(9)]
    for i in range(0,9):
        for j in range(0,9):
            presudoku[i][j].grid(row=i,column=j)
            anssudoku[i][j].grid(row=i+10,column=j)

    tkinter.Button(root,text="run",command=run).grid(row=9,column=1)
    tkinter.Button(root,text="clc",command=clc).grid(row=9,column=3)
    outputlabel=tkinter.Label(root,text="rdy")
    outputlabel.grid(row=9,column=7)

    root.mainloop()

def main():
    sudoku()

if __name__=='__main__':
    main()
