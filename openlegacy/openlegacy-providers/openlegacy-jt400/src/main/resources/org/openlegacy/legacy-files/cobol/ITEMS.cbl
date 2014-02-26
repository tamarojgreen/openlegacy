1.00 130526        IDENTIFICATION DIVISION.                                                 
1.01 131028        DATA DIVISION.                                                           
2.00 131210        PROGRAM-ID. ITEMS.                                                       
2.01 130609        LINKAGE SECTION.                                                         
2.02 131210        01 TOPL.                                                                 
2.03 131217           03 ITEMS      OCCURS 5 TIMES.                                         
2.04 131210              05  NUM               PIC 9(4).                                    
2.05 131210              05  NAME              PIC X(16).                                   
2.06 131217              05  DESCRIPTION       PIC X(28).                                   
3.00 131210        PROCEDURE DIVISION USING TOPL.                                           
3.01 130609        BEGIN.                                                                   
3.02 131210            MOVE 1000 TO NUM(1)                                                  
3.03 131217            MOVE 'Kid Guitar     ' TO NAME(1)                                    
3.04 131217            MOVE 'Kids Guitar - Musical Toys ' TO DESCRIPTION(1).                
3.05 131210            MOVE 1001 TO NUM(2)                                                  
3.06 131217            MOVE 'Ball Pool      ' TO NAME(2)                                    
3.07 131217            MOVE 'Ball Pool - Novelty Toys   ' TO DESCRIPTION(2).                
3.08 131217            MOVE 1002 TO NUM(3)                                                  
3.09 131217            MOVE 'Water Ball     ' TO NAME(3)                                    
3.10 131217            MOVE 'Water Ball - Balls         ' TO DESCRIPTION(3).                
3.11 131217            MOVE 1003 TO NUM(4)                                                  
3.12 131217            MOVE 'Frisbee        ' TO NAME(4)                                    
3.13 131217            MOVE 'Dog Frisbee - Pet Toys     ' TO DESCRIPTION(4).                
3.14 131217            MOVE 1004 TO NUM(5)                                                  
3.15 131217            MOVE 'Pig Bank       ' TO NAME(5)                                    
3.16 131217            MOVE 'Pig Saving Bank - Ceramics ' TO DESCRIPTION(5).                
4.06 131010        HALT.                                                                    
