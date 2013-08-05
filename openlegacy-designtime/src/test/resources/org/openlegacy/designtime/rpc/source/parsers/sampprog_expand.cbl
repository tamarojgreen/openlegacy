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
	   01  C00-SAMPCPY1.                                              
           05 C00-MYVAR                 PIC X(20) VALUE SPACES.         
           05 C00-OTHER-VAR             PIC S9(8) COMP.                 
            88 C00-VAR-SET-OK        VALUE 0.                        
            88 C00-VAR-SET-NOTOK     VALUE 4.                        
            88 C00-VAR-SET-OTHER     VALUE 8.   	 
       LINKAGE SECTION.                                                 
       01  DFHCOMMAREA.                                                 
		 03 CM-VARS.                                       
              05 CM-MYVAR                  PIC X(20).        
              05 CM-OTHER-VAR              PIC S9(9).         
              05 CM-ANOTHER-VAR            PIC X.        
		
       PROCEDURE DIVISION.                                              
                                                                        
            MOVE 'COPY WITH REPLACE' TO C00-MYVAR                       
            MOVE 'COPY WITHOUT REPLACE' TO CM-MYVAR                     
            MOVE 'NOT A COPY' TO WS-SECOND-VAR                          
            SET C00-VAR-SET-OK TO TRUE                                  
                                                                        
            EXEC CICS RETURN END-EXEC                                   
            GOBACK.                                                     
                                                                        