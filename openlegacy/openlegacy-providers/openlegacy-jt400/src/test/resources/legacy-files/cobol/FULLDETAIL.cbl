1.00 130526        IDENTIFICATION DIVISION.                                                 
1.01 131028        DATA DIVISION.                                                           
2.00 131212        PROGRAM-ID. ITEMDETAIL.                                                  
2.01 130609        LINKAGE SECTION.                                                         
2.02 131229           01 ITEM-NUM        PIC 9(8).                                          
2.04 131229           01 ITEM-RECORD.                                                       
2.05 131229             02 ITEM-NAME     PIC X(16).                                         
2.06 131229             02 DESCRIPTION   PIC X(28).                                         
2.07 131229             02 WEIGHT        PIC 9(4).                                          
2.08 131229           01 SHIPPING.                                                          
2.09 140105             02 SHIPPING-METHOD        PIC X(10).                                
2.10 131229             02 DAYS          PIC 9(4).                                          
3.00 131231        PROCEDURE DIVISION USING ITEM-NUM ITEM-RECORD SHIPPING.                  
3.01 131229        BEGIN.                                                                   
3.02 140105            MOVE 'AIR MAIL  ' TO SHIPPING-METHOD.                                
3.03 131229            MOVE 2 TO DAYS.                                                      
3.04 131212            EVALUATE ITEM-NUM                                                    
3.05 131214              WHEN  1000                                                         
3.06 131218                 MOVE 'Kid Guitar     ' TO ITEM-NAME                             
3.07 131218                 MOVE 'Kids Guitar - Musical Toys   ' TO DESCRIPTION             
3.08 131218                 MOVE 200 TO WEIGHT                                              
3.09 131218              WHEN 1001                                                          
3.10 131218                   MOVE 'Ball Pool      ' TO ITEM-NAME                           
3.11 131218                   MOVE 'Ball Pool - Novelty Toys   ' TO DESCRIPTION             
3.12 131218                   MOVE 100 TO WEIGHT                                            
3.13 131218              WHEN 1002                                                          
3.14 131218                   MOVE 'Water Ball     ' TO ITEM-NAME                           
3.15 131218                   MOVE 'Water Ball - Balls         ' TO DESCRIPTION             
3.16 131218                   MOVE 1000 TO WEIGHT                                           
3.17 131218               WHEN 1003                                                         
3.18 131218                   MOVE 'Frisbee        ' TO ITEM-NAME                           
3.19 131218                   MOVE  'Dog Frisbee - Pet Toys     ' TO DESCRIPTION            
3.20 131218                   MOVE 5000 TO WEIGHT                                           
3.21 131218              WHEN 1004                                                          
3.22 131218                   MOVE 'Pig Bank       ' TO ITEM-NAME                           
3.23 131218                   MOVE 'Pig Saving Bank - Ceramics ' TO DESCRIPTION             
3.24 131218                   MOVE 5000 TO WEIGHT                                           
3.25 131212              WHEN OTHER                                                         
3.26 131218                 MOVE 0 TO WEIGHT                                                
3.27 131212                 MOVE 'ERROR          ' TO ITEM-NAME                             
3.28 131218                 MOVE 'OBJECT NOT FOUND          ' TO DESCRIPTION                
3.29 131212            END-EVALUATE.                                                        
4.06 131010        HALT.                                                                    
