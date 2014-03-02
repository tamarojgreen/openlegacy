1.00 130526        IDENTIFICATION DIVISION.                                                 
2.00 130609        PROGRAM-ID ROICBL2.                                                      
2.01 130609        LINKAGE SECTION.                                                         
2.02 130609        01 PARAM1.                                                               
2.03 130817           02 CHILD1 PIC 99.                                                     
2.04 130817           02 CHILD2 PIC 99.                                                     
3.00 130609        PROCEDURE DIVISION USING PARAM1.                                         
3.01 130609        BEGIN.                                                                   
3.03 130817            COMPUTE CHILD2 = CHILD1 + CHILD1.                                    
4.03 130609            STOP RUN.                                                            
4.04 130609        HALT.                                                                    
