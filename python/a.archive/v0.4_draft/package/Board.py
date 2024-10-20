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

'''
Life purpose, business purpose, market opportunity
Entre - undertaking, risk, initiative, effort
Be flexible, not fixed on end goal
Is the impact worth it?
Continuous quality improvement, think slightly differently
Get in the habit of thinking about more options, to be creative you have to be creative everywhere in life
Develop the habits from outside of where you work, develop the mindset
Be more creative
Align business goals with personal values
Pursue meaning and impact over success
Don't focus on success
Fail quickly; opportunity cost; but you have to have the vision, honesty, and lack of ego to see it
Match quiality - does the job match who you are, and how well does it match
Reasons people take a job: money, ambition, social, impact, efficacy (how well are you at it)
Give me a really bad idea, a good idea, an idea that people are already doing, think of a crazy one
If you know an idea is bad, you can avoid it
Pick the path that give syo uthe most options
It's hard today to not havea s specifilty, as people defer to experts, but not let it be at the expense of being a generalist
Personal brand vs product
Read everything everywhere
The more you can speak to people on their terms the better you are positioned
To build rapport with someone who have to show interest in therm, find what makes them excited
No information you should be too good for
Do types of ventures that match your risk tolerance. If you are risk averse, don't be aggressive with risk.
Choices are based on: WHo you are, what you care about, and what you do


--


Not be the achilles heal of the company.
Glucosense: burned a quarter of a million of dollars on overseas software developers.
    Had to rebuild platform when it was developed in angular when they only knew react; it was ummanagable
    Hadn't stopped with customer discovery
    There's always enough time if you're passionate about it
    Build your lifestyle for your business, everything else gets cut
    If a client calls you at 10pm, you're commited 
    You have to love what you do, so that you're not troubled when you are on call
    nexdoor - first listing for 30/hr to help w tech problems
    word of mouth - never spent on marketing
    Don't have your blinders on
    You have to find someone who believes in the vision and is willing to go 24/7
    More people doesn't help; people are problems most of the time
    Don't force fit people into your business
    Believe in what you say. Why are you the right person? You have to know what you are talking about.
    You have to sell them on the vision
    MVP - something to demonstrate, be careful of how you pitch to investors
    
    You can't be slightly better than existing giant companies, you can't step into their turf
    You can't go into the lane of the big existing comapnies - define what you are doing
    
    Focusing on this niche that nobody wants to touch
    
    You can't have hestiation to ask questions
    Two fouders - one business and one technical
    
    y-combinator resources (learn about safenote)
    yc note
    startup exchange
    learn the terms - series A etc
    seed round - don't have as much legal stuff
    defer taxes to liquidity event
    You need to have a long conversation with equity
    equity - vested get it over the year
    
    What has failed, what did you learn
    
    A salary os a bride to stop you from chasing your dreams youtube, google, chatgpt
    
    
Sellraise:
    Now is the age to do it
    Golden handcuffs "I'll start a company after making a lot of money"
    
    Story - had an "AI" app that graded your musical playing. But in reality the person that graded it for them until they made enough money to hire a phd to make an AI algo
    
    VCs love talking to college students because of Zuckerburg
    
    
    ideal customer profile - but when marketing it is more broad - not turning people down
    demographics, psychographics, challenges/pain points, solutions, behaviors
    
    research your audience, segment your audience, deinfe the charactersitics, identify pain points and needs, create a persona (with a name)
    
    
'''