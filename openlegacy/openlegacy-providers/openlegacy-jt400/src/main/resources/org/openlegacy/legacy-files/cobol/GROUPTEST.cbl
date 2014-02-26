1.00 130526        IDENTIFICATION DIVISION.                                                 
2.00 140219        PROGRAM-ID. GRROUP.                                                      
2.01 130609        LINKAGE SECTION.                                                         
2.02 131009        01 PARAM1.                                                               
2.04 131009           03 CHILD1 PIC 99.                                                     
2.05 131009           03 CHILD2 PIC 99.                                                     
2.06 140219           03 GROUP1CHILD1 PIC 99.                                               
2.07 140219           03 GROUP1CHILD2 PIC 99.                                               
2.08 140219           03 GROUP1CHILD3 PIC 99.                                               
2.09 140219           03 GROUP2CHILD1 PIC 99.                                               
2.10 140219           03 GROUP2CHILD2 PIC 99.                                               
3.00 130609        PROCEDURE DIVISION USING PARAM1.                                         
3.01 130609        BEGIN.                                                                   
3.03 130817            COMPUTE CHILD2 = CHILD1 + CHILD1.                                    
3.04 140219            COMPUTE  GROUP1CHILD3 =  GROUP1CHILD1 +  GROUP1CHILD2.               
3.05 140219            COMPUTE GROUP2CHILD2 =  GROUP2CHILD1 + GROUP2CHILD2.                 
4.03 130609            STOP RUN.                                                            
4.04 130609        HALT.                                                                    
