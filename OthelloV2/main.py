#Created by Alex Aridgides



'''Start variables'''



'''Change this value to True to play in the console!'''
consoleGame = True
'''Change this value to True to play in the console!'''

version = '2.20'
lastUpdate = '07/14/2024'
lastUpdateFormat = 'mm/dd/yyyy'
#Or in the app click on play in console


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


#black pieces, white pieces
score = [2, 2]

#moves executed, 0 = game has not started, 60 = game has definitely ended
moves = 0

#who has to make a move
turn = 'Black'

#what piece is being played
piece = 0

#the opponent's piece to be flipped
opponentPiece = 1

#disks to be flipped
flippedList = []

#positions where black can move
blackPossibleMoves = []

#positions where white can move
whitePossibleMoves = []



'''Functions'''



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


def validMove(player, y, x):
    #determine if a move is valid and the disks that would move
    #input: who is making move, move position, output: bool, changed: flippedList changed
    
    global flippedList
    global piece
    global opponentPiece
    
    sidesFlipped = 0
    flippedList = []
    
    pieceFunc(player)
    
    if board[y][x] == 2:
        
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
                    flippedList.extend(tempFlippedList)
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
                    flippedList.extend(tempFlippedList)
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
                    flippedList.extend(tempFlippedList)
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
                    flippedList.extend(tempFlippedList)
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
                    flippedList.extend(tempFlippedList)
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
                    flippedList.extend(tempFlippedList)
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
                    flippedList.extend(tempFlippedList)
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
                    flippedList.extend(tempFlippedList)
                    break


    if sidesFlipped != 0:
        return True
    flippedList = []
    return False


def movePositions():
    #update moves that each player can make
    #input: none, output: none, change: blackPossibleMoves and whitePossibleMoves
    
    global blackPossibleMoves
    global whitePossibleMoves
    global flippedList
    
    blackPossibleMoves = []
    whitePossibleMoves = []
    
    for q in range(8):
        for w in range(8):
            if validMove('Black', q, w) == True:
                blackPossibleMoves.append([q, w])
    
    for q in range(8):
        for w in range(8):
            if validMove('White', q, w) == True:
                whitePossibleMoves.append([q, w])

    flippedList = []


def canMove(player):
    #determine if a player can make a move
    #input: player, output: bool, change: none
    
    movePositions()
    if len(blackPossibleMoves) != 0 and player == 'Black':
        return True
    elif len(whitePossibleMoves) != 0 and player == 'White':
        return True
    return False


def pieceFunc(player):
    global piece
    global opponentPiece
    
    if player == 'Black':
        piece = 0
        opponentPiece = 1
    else:
        piece = 1
        opponentPiece = 0


def move(player, y, x):
    #change disks on the board and change turns, update data
    #input player, space, output none, change board, score, possibleMoves
    
    global piece
    global opponentPiece
    global turn
    global moves
    
    validMove(player, y, x)
    
    board[y][x] = piece
    for q in range(len(flippedList)):
        board[flippedList[q][0]][flippedList[q][1]] = piece
    
    moves += 1
    calcScore()
    if canMove('Black') == False or turn == 'Black':
        turn = 'White'
        piece = 1
        opponentPiece = 0
    else:
        turn = 'Black'
        piece = 0
        opponentPiece = 1


firstBoardOut = True


def clear_line(n):
    LINE_UP = '\033[1A'
    LINE_CLEAR = '\x1b[2K'
    for i in range(n):
        print(LINE_UP, end=LINE_CLEAR)


def printBoard():
    
    calcScore
    
    text = f'       Black  White\nScore: {score[0]}      {score[1]}        Turn: {turn}\n\n    a   b   c   d   e   f   g   h\n'
    
    for q in range(8):
        text += str(q+1) + ' |'
        
        for w in range(8):
            if board[q][w] == 2:
                text += ' . |'
                
            elif board[q][w] == 0:
                text += ' B |'
                
            else:
                text += ' W |'
                
        text += '\n'
    
    global firstBoardOut
    if not firstBoardOut:
        clear_line(14)
    print(text)
    firstBoardOut = False

#To play in console, change this value at the top to True
if consoleGame == True:
    
    print(f'''
      Start game in console! Human vs human currently supported. Version {version}.
      
      Layout:
      B: Black disk
      W: White disk
      .: Blank Square
      
      Controls:
      When prompted, input a valid square through a lowercase letter and then a number with no spaces (ie. 'a4')
      
      Input 'exit' to stop
      
      Have fun!
      ''')
    printBoard()


    breaker = False
    userInput = ''

    while breaker == False:
        userInput = input('Input square:')
        userInput = str(userInput).strip()
        
        t = ord(str(userInput[0].lower())) - 97
        
        if userInput == 'exit':
            breaker = True
            print('You have exited the game. Thank you for playing!')
            break
        elif t < 0 or t > 7 or userInput[0].isalpha() == False or userInput[1].isnumeric() == False or int(userInput[1]) > 8 or int(userInput[1]) < 1 or len(userInput) != 2:
            print('Invalid input. Please try again with the format letter + number on a valid square (a-h and 1-8, inclusive). ie. \'a4\'')
        else:
            userInput = [int(userInput[1]) - 1, t]
            
            if validMove(turn, userInput[0], userInput[1]) == True:
                move(turn, userInput[0], userInput[1])
                printBoard()
                
            else:
                print('Choose a valid move position.')