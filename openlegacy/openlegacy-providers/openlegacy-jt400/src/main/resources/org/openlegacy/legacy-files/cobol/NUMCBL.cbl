1.00 130526        IDENTIFICATION DIVISION.                                                 
2.00 131009        PROGRAM-ID. ROICBL2.                                                     
2.01 130609        LINKAGE SECTION.                                                         
2.02 131009                                                                                 
2.03 131104           01 CHILD1 PIC 9999.                                                   
2.04 131104           01 CHILD2 PIC 9999.                                                   
3.00 131009        PROCEDURE DIVISION USING CHILD1 CHILD2.                                  
3.01 130609        BEGIN.                                                                   
3.03 130817            COMPUTE CHILD2 = CHILD1 + CHILD1.                                    
4.03 130609            STOP RUN.                                                            
4.04 130609        HALT.                                                                    
