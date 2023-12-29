#Play
#AI
#create puzzles



'''Start variables'''



#empty square: 2
#white piece: 1
#black piece: 0
board = [[2, 2, 2, 2, 2, 2, 2, 2],
        [2, 2, 2, 2, 2, 2, 2, 2],
        [2, 2, 2, 2, 2, 2, 2, 2],
        [2, 2, 2, 0, 1, 2, 2, 2],
        [2, 2, 2, 1, 0, 2, 2, 2],
        [2, 2, 2, 2, 2, 2, 2, 2],
        [2, 2, 2, 2, 2, 2, 2, 2],
        [2, 2, 2, 2, 2, 2, 2, 2]]


#used in determining move trees
virtualBoard = [[2, 2, 2, 2, 2, 2, 2, 2],
                [2, 2, 2, 2, 2, 2, 2, 2],
                [2, 2, 2, 2, 2, 2, 2, 2],
                [2, 2, 2, 0, 1, 2, 2, 2],
                [2, 2, 2, 1, 0, 2, 2, 2],
                [2, 2, 2, 2, 2, 2, 2, 2],
                [2, 2, 2, 2, 2, 2, 2, 2],
                [2, 2, 2, 2, 2, 2, 2, 2]]


boardXLabels = ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h']

boardYLabels = ['1', '2', '3', '4', '5', '6', '7', '8']


#black pieces, white pieces
score = [2, 2]


#moves executed, 0 = game has not started, 60 = game has definitely ended
move = 0


#who has to make a move
turn = 'Black'

#what piece is being played
piece = 0

#the opponent's piece to be flipped
opponentPiece = 1

#disks to be flipped
flippedList = []



'''Functions'''



#checked
def calcScore():
    #calculate current score, but do not display it
    #input: none, output: sum of pieces on board, changed: score variable
    
    global score
    blackScore = 0
    whiteScore = 0
    
    for q in range(8):
        blackScore += board[q].count(0)
        whiteScore += board[q].count(1)
    
    score = [blackScore, whiteScore]
    
    return score[0] + score[1]


board = [[2, 2, 2, 2, 2, 2, 2, 2],
         [2, 2, 0, 2, 2, 2, 2, 2],
         [0, 2, 1, 2, 0, 2, 2, 2],
         [2, 2, 2, 1, 1, 2, 1, 0],
         [0, 2, 2, 2, 1, 1, 0, 2],
         [2, 2, 2, 2, 2, 2, 2, 2],
         [0, 2, 0, 2, 1, 2, 2, 2],
         [2, 2, 2, 2, 2, 0, 2, 2]]

#right turn, flip at least 1 opponent disk (empty square, check all directions)

def move(player, y, x):
    #determine if a move is valid
    #input: who is making move, move position, output: bool, changed: none
    
    global flippedList
    
    sidesFlipped = 0
    flippedList = []
    
    
    if player == turn and board[y][x] == 2:
        
        #check right
        flipped = 0
        tempFlippedList = []
        
        if x < 6 and board[y][x+1] != piece:
            for q in range(1, 7-x):
                if board[y][x+q] == 2:
                    break
                
                elif board[y][x+q] == opponentPiece:
                    if x+q != 7:
                        flipped += 1
                        tempFlippedList.append([y, x+q])
                        
                    else:
                        break
                    
                elif flipped == 0:
                    break
                
                else:
                    sidesFlipped += 1
                    for w in tempFlippedList:
                        flippedList.append(w)
                    break
        
        
        #check left
        flipped = 0
        tempFlippedList = []
        
        if x > 1 and board[y][x-1] != piece:
            for q in range(1, x+1):
                if board[y][x-q] == 2:
                    break
                    
                elif board[y][x-q] == opponentPiece:
                    if x-q != 0:
                        flipped += 1
                        tempFlippedList.append([y, x-q])
                        
                    else:
                        break
                        
                elif flipped == 0:
                    break
                
                else:
                    sidesFlipped += 1
                    for w in tempFlippedList:
                        flippedList.append(w)
                    break
        
        
        #check top
        flipped = 0
        tempFlippedList = []
        
        if y > 1 and board[y-1][x] != piece:
            for q in range(1, y+1):
                if board[y-q][x] == 2:
                    break
                    
                elif board[y-q][x] == opponentPiece:
                    if y-q != 0:
                        flipped += 1
                        tempFlippedList.append([y-q, x])
                        
                    else:
                        break
                        
                elif flipped == 0:
                    break
                
                else:
                    sidesFlipped += 1
                    for w in tempFlippedList:
                        flippedList.append(w)
                    break
        
        
        #check bottom
        flipped = 0
        tempFlippedList = []
        
        if y < 6 and board[y+1][x] != piece:
            for q in range(1, 7-y):
                if board[y+q][x] == 2:
                    break
                
                elif board[y+q][x] == opponentPiece:
                    if y+q != 7:
                        flipped += 1
                        tempFlippedList.append([y+q, x])
                        
                    else:
                        break
                    
                elif flipped == 0:
                    break
                
                else:
                    sidesFlipped += 1
                    for w in tempFlippedList:
                        flippedList.append(w)
                    break
        
        
        #check bottom right
        flipped = 0
        tempFlippedList = []
        
        if y < 6 and x < 6 and board[y+1][x+1] != piece:
            
            for q in range(1, 8):
                if board[y+q][x+q] == 2:
                    break
                
                elif board[y+q][x+q] == opponentPiece:
                    if y+q != 7 and x+q != 7:
                        flipped += 1
                        tempFlippedList.append([y+q, x+q])
                        
                    else:
                        break
                    
                elif flipped == 0:
                    break
                
                else:
                    sidesFlipped += 1
                    for w in tempFlippedList:
                        flippedList.append(w)
                    break
        
        
        #check bottom left
        flipped = 0
        tempFlippedList = []
        
        if y < 6 and x > 1 and board[y+1][x-1] != piece:
            
            for q in range(1, 8):
                if board[y+q][x-q] == 2:
                    break
                
                elif board[y+q][x-q] == opponentPiece:
                    if y+q != 7 and x-q != 0:
                        flipped += 1
                        tempFlippedList.append([y+q, x-q])
                        
                    else:
                        break
                    
                elif flipped == 0:
                    break
                
                else:
                    sidesFlipped += 1
                    for w in tempFlippedList:
                        flippedList.append(w)
                    break
        
        
        #check top left
        flipped = 0
        tempFlippedList = []
        
        if y > 1 and x > 1 and board[y-1][x-1] != piece:
            
            for q in range(1, 8):
                if board[y-q][x-q] == 2:
                    break
                
                elif board[y-q][x-q] == opponentPiece:
                    if y-q != 0 and x-q != 0:
                        flipped += 1
                        tempFlippedList.append([y-q, x-q])
                        
                    else:
                        break
                    
                elif flipped == 0:
                    break
                
                else:
                    sidesFlipped += 1
                    for w in tempFlippedList:
                        flippedList.append(w)
                    break
        
        
        #check top right
        flipped = 0
        tempFlippedList = []
        
        if y > 1 and x < 7 and board[y-1][x+1] != piece:
            
            for q in range(1, 8):
                if board[y-q][x+q] == 2:
                    break
                
                elif board[y-q][x+q] == opponentPiece:
                    if y-q != 0 and x+q != 7:
                        flipped += 1
                        tempFlippedList.append([y-q, x+q])
                        
                    else:
                        break
                    
                elif flipped == 0:
                    break
                
                else:
                    sidesFlipped += 1
                    for w in tempFlippedList:
                        flippedList.append(w)
                    break


    if sidesFlipped != 0:
        return True
    flippedList = []
    return False


print(move('Black', 4, 2))
print(flippedList)



'''Main'''



m = 0



'''Tests and Old Code'''



'''def position(squareX, squareY):
    #return move position on board
    #input: squareX and squareY, output: board position in list

    return squareY * 8 + squareX


def move(player, squareX, squareY):
    #make a move on the board and check result
    #input: who is making the move, move position, output: score, changed: virtual board
    tempBoard = board
        


print(board[move('Black', 3, 4)])

board = []

for q in range(64):
    board.append(q)

print(board)

def test():
    global some
    some = 0
    for q in range(8):
        for w in range(8):
            print(board[move('Black', w, q)])
test()'''