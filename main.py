#Play
#AI
#create puzzles

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

#black pieces, white pieces
score = [2, 2]

#moves executed, 0 = game has not started, 60 = game has definitely ended
move = 0
turn = 'Black'

#calculate current score, but do not display it
#input: none, output: sum of pieces on board, changed: score variable
def calcScore():
    '''auto doc string'''
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