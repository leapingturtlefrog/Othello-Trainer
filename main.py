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

#calculate current score, but do not display it
def calcScore():
    blackScore = 0
    whiteScore = 0
    for q in board:
        if q == 0:
            blackScore = blackScore + 1
        elif q == 1:
            whiteScore = whiteScore + 1
    score = [blackScore, whiteScore]