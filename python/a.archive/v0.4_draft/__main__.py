from package import src.othellotrainer.Board

if __name__ == "__main__":
    board = src.othellotrainer.Board()

    print(board.get_board().itemsize)
    
    board.get_disks_to_flip('Black', 4, 2)
    
    board.move('Black', 4, 2)
