IDENTIFICATION DIVISION.
PROGRAM-ID. TREEARRAY.
DATA DIVISION.                                                     
LINKAGE SECTION.                                                         
01 AR-ARRAY.                                                             
  03 AR-ARRAY-RECORDS      OCCURS 3 TIMES.                              
    05  AR-TEXT              PIC X(11).                                
    05  AR-NUM               PIC 9(4).                                 
PROCEDURE DIVISION USING AR-ARRAY.                                       
BEGIN.                                                                   
      MOVE 30 TO AR-NUM(1)                                                 
      MOVE 40 TO AR-NUM(2)                                                 
      COMPUTE AR-NUM(3) = AR-NUM(1) + AR-NUM(2).                           
      MOVE 'ARRAY ONE  ' TO AR-TEXT(1).                                    
      MOVE 'ARRAY TOW  ' TO AR-TEXT(2).                                    
      MOVE 'ARRAY THREE' TO AR-TEXT(3).                                    
      DISPLAY 'VALUE 1:' AR-NUM(1).                                        
HALT.         