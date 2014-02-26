1.00 130526        IDENTIFICATION DIVISION.                                                 
1.01 131028        DATA DIVISION.                                                           
2.00 131010        PROGRAM-ID. TREEARRAY.                                                   
2.01 130609        LINKAGE SECTION.                                                         
2.02 131010        01 AR-ARRAY.                                                             
2.03 131010           03 AR-ARRAY-RECORDS      OCCURS 3 TIMES.                              
2.04 131013              05  AR-TEXT              PIC X(11).                                
2.05 131010              05  AR-NUM               PIC 9(4).                                 
3.00 131010        PROCEDURE DIVISION USING AR-ARRAY.                                       
3.01 130609        BEGIN.                                                                   
3.02 131013            MOVE 30 TO AR-NUM(1)                                                 
3.03 131013            MOVE 40 TO AR-NUM(2)                                                 
3.04 131010            COMPUTE AR-NUM(3) = AR-NUM(1) + AR-NUM(2).                           
3.05 131010            MOVE 'ARRAY ONE  ' TO AR-TEXT(1).                                    
4.03 131013            MOVE 'ARRAY TOW  ' TO AR-TEXT(2).                                    
4.04 131013            MOVE 'ARRAY THREE' TO AR-TEXT(3).                                    
4.05 131013            DISPLAY 'VALUE 1:' AR-NUM(1).                                        
4.06 131010        HALT.                                                                    
