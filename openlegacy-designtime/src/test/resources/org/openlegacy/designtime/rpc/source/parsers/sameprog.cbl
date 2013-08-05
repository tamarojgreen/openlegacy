       IDENTIFICATION DIVISION.                                         
       PROGRAM-ID. SAMPPROG.                                            
      *======================================================*          
      * SAMPLE PROGRAM THAT DOES VERY LITTLE                 *          
      *======================================================*          
       ENVIRONMENT DIVISION.                                            
       DATA DIVISION.                                                   
       WORKING-STORAGE SECTION.                                         
       01  WS-VARS.                                                     
           05 WS-FIRST-VAR          PIC S9(8) COMP VALUE ZERO.          
           05 WS-SECOND-VAR         PIC X(20) VALUE SPACE.              
       COPY SAMPCPY1                                                    
           REPLACING ==:XXX:==  BY  ==C00--==.                         
       LINKAGE SECTION.                                                 
       01  DFHCOMMAREA.                                                 
           COPY SAMPCPY2.                                               
       PROCEDURE DIVISION.                                              
                                                                        
            MOVE 'COPY WITH REPLACE' TO C00-MYVAR                       
            MOVE 'COPY WITHOUT REPLACE' TO CM-MYVAR                     
            MOVE 'NOT A COPY' TO WS-SECOND-VAR                          
            SET C00-VAR-SET-OK TO TRUE                                  
                                                                        
            EXEC CICS RETURN END-EXEC                                   
            GOBACK.                                                     
                                                                        