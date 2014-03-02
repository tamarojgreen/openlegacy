1.00 130526        IDENTIFICATION DIVISION.                                                 
1.01 131028        DATA DIVISION.                                                           
2.00 131212        PROGRAM-ID. ITEMDETAIL.                                                  
2.01 130609        LINKAGE SECTION.                                                         
2.02 131218           01 ITEM-NUM      PIC 9(8).                                            
2.05 131212           01 ITEM-NAME     PIC X(16).                                           
2.06 131218           01 DESCRIPTION   PIC X(28).                                           
2.07 131218           01 WEIGHT   PIC 9(4).                                                 
3.00 131212        PROCEDURE DIVISION USING ITEM-NUM ITEM-NAME DESCRIPTION WEIGHT.          
3.01 131212        BEGIN.                                                                   
3.02 131212            EVALUATE ITEM-NUM                                                    
3.03 131214              WHEN  1000                                                         
3.04 131218                 MOVE 'Kid Guitar     ' TO ITEM-NAME                             
3.05 131218                 MOVE 'Kids Guitar - Musical Toys   ' TO DESCRIPTION             
3.06 131218                 MOVE 200 TO WEIGHT                                              
3.07 131218              WHEN 1001                                                          
3.08 131218                   MOVE 'Ball Pool      ' TO ITEM-NAME                           
3.09 131218                   MOVE 'Ball Pool - Novelty Toys   ' TO DESCRIPTION             
3.10 131218                   MOVE 100 TO WEIGHT                                            
3.11 131218              WHEN 1002                                                          
3.12 131218                   MOVE 'Water Ball     ' TO ITEM-NAME                           
3.13 131218                   MOVE 'Water Ball - Balls         ' TO DESCRIPTION             
3.14 131218                   MOVE 1000 TO WEIGHT                                           
3.15 131218               WHEN 1003                                                         
3.16 131218                   MOVE 'Frisbee        ' TO ITEM-NAME                           
3.17 131218                   MOVE  'Dog Frisbee - Pet Toys     ' TO DESCRIPTION            
3.18 131218                   MOVE 5000 TO WEIGHT                                           
3.19 131218              WHEN 1004                                                          
3.20 131218                   MOVE 'Pig Bank       ' TO ITEM-NAME                           
3.21 131218                   MOVE 'Pig Saving Bank - Ceramics ' TO DESCRIPTION             
3.22 131218                   MOVE 5000 TO WEIGHT                                           
3.23 131212              WHEN OTHER                                                         
3.24 131218                 MOVE 0 TO WEIGHT                                                
3.25 131212                 MOVE 'ERROR          ' TO ITEM-NAME                             
3.26 131218                 MOVE 'OBJECT NOT FOUND          ' TO DESCRIPTION                
3.27 131212            END-EVALUATE.                                                        
4.06 131010        HALT.                                                                    
