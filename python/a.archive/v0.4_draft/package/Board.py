import numpy as np
import pandas as pd


class src.othellotrainer.Board:
    '''Contains the board, positions, and history for a given board'''
    
    def __init__(self):
        '''Sets up the standard starting board'''
        
        #empty square: 2
        #white piece: 1
        #black piece: 0
        self.curr_board = np.array(
            [
                [2, 2, 2, 2, 2, 2, 2, 2],
                [2, 2, 2, 2, 2, 2, 2, 2],
                [2, 2, 2, 2, 2, 2, 2, 2],
                [2, 2, 2, 0, 1, 2, 2, 2],
                [2, 2, 2, 1, 1, 1, 0, 2],
                [2, 2, 1, 2, 2, 2, 2, 2],
                [2, 2, 0, 2, 2, 2, 2, 2],
                [2, 2, 2, 2, 2, 2, 2, 2]
            ]
        )
        
        '''
        [
            [2, 2, 2, 2, 2, 2, 2, 2],
            [2, 2, 2, 2, 2, 2, 2, 2],
            [2, 2, 2, 2, 2, 2, 2, 2],
            [2, 2, 2, 0, 1, 2, 2, 2],
            [2, 2, 2, 1, 0, 2, 2, 2],
            [2, 2, 2, 2, 2, 2, 2, 2],
            [2, 2, 2, 2, 2, 2, 2, 2],
            [2, 2, 2, 2, 2, 2, 2, 2]
        ]
        '''
        
        #main tracker of current and past data
        self.data = pd.DataFrame(
            {
                'move_number' : 0,
                'board' : [self.curr_board],
                'score' : [{'Black': 2, 'White': 2}],
                'player_to_move' : 'Black',
                'move_history': '',
                'game_ended' : False  
            }
        )
    
    def get_move_number(self):
        return self.data.iloc[-1]['move_number']
    
    def get_board(self, move_number=-1):
        return self.data.iloc[move_number]['board']
        
    def get_score(self):
        self.update_score()
        
        return self.data.iloc[-1]['score']
    
    def is_game_ended(self):
        return self.data.iloc[-1]['game_ended']
    
    def move(self, player, row, col):
        '''
        Makes a move for a player on a row and column. Assumes the position
        is a valid row or column, but not necessarily a valid move in the game
        
        Parameters
            player : str
                Whether the player is 'black' or 'White'
            row : int
                The row to place the disk. 0-7
            col : int
                The column to place the disk. 0-7
        
        Return
            bool
                Whether or not the move was successful (ie. legal)
        '''
        num = self._get_player_number(player)
        disks_to_flip = self.get_disks_to_flip(player, row, col)
        
        if disks_to_flip != []:
            self.curr_board[row, col] = num
            for pos in disks_to_flip:
                self.curr_board[pos[0], pos[1]] = num
            
            self.update_data(self.get_move_text(player, row, col))
        else:
            return False
    
    def update_data(self, move_text):
        curr_data = self.data[-1]
        
        self.data = pd.concat(
            [self.data,
                {
                    'move_number' : curr_data['move_number'] + 1,
                    'board' : [self.curr_board],
                    'score' : [self.get_score()],
                    'player_to_move' : self.get_player_to_move(),
                    'move_history': curr_data['move_history'] + move_text,
                    'game_ended' : self.is_game_ended() 
                }
            ]
        )
    
    def get_disks_to_flip(self, player, row, col):
        '''
        Return a list of the positions of the disks to be flipped
        
        Parameters
            player : str
                Whether the player is 'Black' or 'White'
            row : int
                The row to place the disk. 0-7
            col : int
                The column to place the disk. 0-7
        
        Return
            List[List[int]]
                A list of the [row, col] of the disks to be flipped. Empty if there are none
        '''
        num = self._get_player_number(player)
        disks_to_flip = []
        row_list = [0,  0, 1, -1, 1, 1, -1, -1]
        col_list = [1, -1, 0,  0, 1, -1, 1, -1]
        
        for i in range(8):
            disks_to_flip += self.get_disks_to_flip_line(num, row, col, row_list[i], col_list[i])
        
        print(disks_to_flip)
        
        return disks_to_flip
    
    def get_disks_to_flip_line(self, num, row, col, row_increment, col_increment):
        opposite_num = 1 if num == 0 else 0
        disks_to_flip = []
        
        row += row_increment
        col += col_increment
        
        while row < 8 and row > -1 and col < 8 and col > -1:
            curr_spot = self.curr_board[row, col]
            if curr_spot == 2:
                return []
            elif curr_spot == opposite_num:
                disks_to_flip.append([row, col])
            else:
                return disks_to_flip
            
            row += row_increment
            col += col_increment
        
        return []
    
    
    def update_score(self):
        self.data.at[-1, 'score']['Black'] = len(self.curr_board[self.curr_board == 0])
        self.data.at[-1, 'score']['White'] = len(self.curr_board[self.curr_board == 1])
        
        return self.data.iloc[-1]['score']
    
    def _get_player_number(self, player):
        return 0 if player == 'Black' else 1
    
    def _get_opposing_player_number(self, player):
        return 1 if player == 'Black' else 0
