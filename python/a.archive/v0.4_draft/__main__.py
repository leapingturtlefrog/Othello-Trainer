from package import othellotrainer.Board

if __name__ == "__main__":
    board = othellotrainer.Board()

    print(board.get_board().itemsize)
    
    board.get_disks_to_flip('Black', 4, 2)
    
    board.move('Black', 4, 2)
