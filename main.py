#Play
#AI
#create puzzles



'''Start variables'''



#empty square: 2
#white piece: 1
#black piece: 0
board = [2, 2, 2, 2, 2, 2, 2, 2,
         2, 2, 2, 2, 2, 2, 2, 2,
         2, 2, 2, 2, 2, 2, 2, 2,
         2, 2, 2, 0, 1, 2, 2, 2,
         2, 2, 2, 1, 0, 2, 2, 2,
         2, 2, 2, 2, 2, 2, 2, 2,
         2, 2, 2, 2, 2, 2, 2, 2,
         2, 2, 2, 2, 2, 2, 2, 2,]


#used in determining move trees
virtualBoard = [2, 2, 2, 2, 2, 2, 2, 2,
                2, 2, 2, 2, 2, 2, 2, 2,
                2, 2, 2, 2, 2, 2, 2, 2,
                2, 2, 2, 0, 1, 2, 2, 2,
                2, 2, 2, 1, 0, 2, 2, 2,
                2, 2, 2, 2, 2, 2, 2, 2,
                2, 2, 2, 2, 2, 2, 2, 2,
                2, 2, 2, 2, 2, 2, 2, 2,]


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



'''Functions'''



def calcScore():
    #calculate current score, but do not display it
    #input: none, output: sum of pieces on board, changed: score variable
    
    blackScore = 0
    whiteScore = 0
    for q in board:
        if q == 0:
            blackScore += 1
        elif q == 1:
            whiteScore += 1
    global score
    score = [blackScore, whiteScore]
    return blackScore + whiteScore


def move(player, squareX, squareY):
    #make a move on the board and check result
    #input: who is making the move, move position, output: score, changed: virtual board
    tempBoard = board
    
    if player == turn:
        m = 0
        
    else:
        return False


def validMove(player, square):
    #determine if a move is valid
    #input: who is making move, move position, output: bool, changed: none
    m = 0

'''Main'''



