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
    
    piece(player)
    
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


def piece(player):
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
    
    validMove(player, y, x)
    
    board[y][x] = piece
    for q in range(len(flippedList)):
        board[flippedList[q][0]][flippedList[q][1]] = piece
    
    #call dentist
    #call Robinhood, other broker
    #stocks
    #MIT app
    #essay
    #table parts, order


def printBoard():
    
    text = '    a   b   c   d   e   f   g   h\n'
    
    for q in range(8):
        text += str(q+1) + ' |'
        
        for w in range(8):
            if board[q][w] == 2:
                text += ' - |'
                
            elif board[q][w] == 0:
                text += ' B |'
                
            else:
                text += ' W |'
                
        text += '\n'
    
    print(text)


move('Black', 4, 2)
printBoard()


'''Main'''



import sys
from PyQt6.QtWidgets import (
    QApplication,
    QWidget,
    QLabel,
    QLineEdit,
    QPushButton,
    QVBoxLayout
)


class MainWindow(QWidget):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)

        # set the window title
        self.setWindowTitle('Othello Study Trainer')

        # create a button widget and connect its clicked signal
        # to a method
        button = QPushButton('Start Game')
        button.clicked.connect(self.buttonClicked)
        
        label = QLabel()
        lineEdit = QLineEdit()
        lineEdit.textChanged.connect(label.setText)


        # place the button on window using a vertical box layout
        layout = QVBoxLayout()
        self.setLayout(layout)

        layout.addWidget(button)
        layout.addWidget(label)
        layout.addWidget(line_edit)
        
        # show the window
        self.show()

    def buttonClicked(self):
        print('clicked')


if __name__ == '__main__':
    app = QApplication(sys.argv)

    # create the main window and display it
    window = MainWindow()

    # start the event loop
    sys.exit(app.exec())


#sender_object.signal_name.connect(receiver_object.slot_name)



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