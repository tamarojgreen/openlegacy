     ************************************************************************
     * (������ ���� �����)!HFLIB× ��� ELBPR �����× ����� ×��� ����×���*******
     ************************************************************************
       PROCESS NOMONOPRC  APOST.
       IDENTIFICATION DIVISION.
      *=================================================================
       PROGRAM-ID.                     ESUSERB.
       AUTHOR.                         BATYAK.
       INSTALLATION.                   AS400.
          DATE-WRITTEN.                09/04/2008.
          DATE-COMPILED.
      *REMARKS.
      *         ������� ��×�� ����� ����� ���� ��×� ����� - ÷���� �����
      *                                                2003 :���� �����
      * AMLA                          �×�× ������� ������ ������ :�����
      *     (���� �� ����� �� �÷ ,����× ���� ������) ��/�� �����
      *---                            ������� ����× ������ ������ ����×
      *                                �����.1 -PO2_ESERSG :����÷� ����
      *                                �����.2
      *       1 = PO2_ESERSG �� �� ����� ���÷� -PO2_ESKERR
      * ISO                                         iso   ������ :�����
      * RONBIT                                       ��×��� ���� :�����
      * DIRA                                ����� ������� ������ :�����
      * amalot                    ����� ��� '× ���� �� ����� �÷�� ���÷�
      *=================================================================
       ENVIRONMENT DIVISION.
      *=================================================================
       CONFIGURATION SECTION.
       SOURCE-COMPUTER. AS-400.
       OBJECT-COMPUTER. AS-400.
       SPECIAL-NAMES. DATA-AREA  IS DATA-AREA
                      LOCAL-DATA IS LDA-AREA1
                   LINKAGE TYPE PROCEDURE FOR "QlnSetCobolErrorHandler".
      *-----------------------------------------------------------------
       INPUT-OUTPUT SECTION.
      *-----------------------------------------------------------------
       FILE-CONTROL.
      *-------------*
           SELECT  USERSS1           ASSIGN TO DATABASE-USERSS1
                   ORGANIZATION      IS INDEXED
                   ACCESS MODE       IS DYNAMIC
                   RECORD KEY        IS EXTERNALLY-DESCRIBED-KEY
                   WITH DUPLICATES
                   FILE STATUS       IS SW-STATUS.
           SELECT  ELNTUSR           ASSIGN TO DATABASE-ELNTUSR
                   ORGANIZATION      IS INDEXED
                   ACCESS MODE       IS DYNAMIC
                   RECORD KEY        IS EXTERNALLY-DESCRIBED-KEY
                   FILE STATUS       IS SW-STATUS.
           SELECT  USERSANF          ASSIGN TO DATABASE-USERSANF
                   ORGANIZATION      IS INDEXED
                   ACCESS MODE       IS DYNAMIC
                   RECORD KEY        IS EXTERNALLY-DESCRIBED-KEY
                   FILE STATUS       IS SW-STATUS.
  YDD      SELECT  ELMAVL            ASSIGN TO DATABASE-ELMAVL
                   ORGANIZATION      IS INDEXED
                   ACCESS MODE       IS DYNAMIC
                   RECORD KEY        IS EXTERNALLY-DESCRIBED-KEY
                   FILE STATUS       IS SW-STATUS.
  YDD      SELECT  RSOCENF
                   ASSIGN TO           DATABASE-HHLLC001
                   ORGANIZATION IS     INDEXED
                   ACCESS MODE  IS     DYNAMIC
                   RECORD KEY   IS     EXTERNALLY-DESCRIBED-KEY
                   FILE STATUS  IS     SW-STATUS.
           SELECT  ESMAVL            ASSIGN TO DATABASE-ESMAVL
                   ORGANIZATION      IS INDEXED
                   ACCESS MODE       IS DYNAMIC
                   RECORD KEY        IS EXTERNALLY-DESCRIBED-KEY
                   WITH DUPLICATES
                   FILE STATUS       IS SW-STATUS.
      *=================================================================
       DATA DIVISION.
      *=================================================================
       FILE SECTION.
      *-------------*
       FD USERSS1
                   LABEL RECORD IS STANDARD.
       01 USERSS1-REC.
          COPY DDS-ALL-FORMATS OF USERSS1.
       FD ELNTUSR
                   LABEL RECORD IS STANDARD.
       01 ELNTUSR-REC.
          COPY DDS-ALL-FORMATS OF ELNTUSR.
       FD USERSANF
                   LABEL RECORD IS STANDARD.
       01 USERSANF-REC.
          COPY DDS-ALL-FORMATS OF USERSANF.
      *-----------------                                               -
 YDD   FD ELMAVL
                   LABEL RECORD IS STANDARD.
       01 ELMAVL-REC.
           COPY DDS-ALL-FORMAT OF ELMAVL.
      *-------------
 YDD   FD   RSOCENF
            LABEL RECORD IS STANDARD.
       01   RSOCEN-REC.
            COPY DDS-ALL-FORMAT OF HHLLC001.
       FD   ESMAVL
            LABEL RECORD IS STANDARD.
       01   ESMAVL-REC.
            COPY DDS-ALL-FORMAT OF ESMAVL.
      *-----------------------------------------------------------------
       WORKING-STORAGE SECTION.
      *-----------------------------------------------------------------
      *-------------*
      *                                ERROR-HANDLER   DATA.
      *-------------*
           COPY QUSEC OF QSYSINC-QCBLLESRC.
       01  ERROR-HANDLER       PROCEDURE-POINTER.
       01  OLD-ERROR-HANDLER   PROCEDURE-POINTER.
      *-------------*
      * EZXX-���� ���
      *-------------*
       01 EZ00.
          03 EZ-ERR                    PIC 9     VALUE 0.
          03 EZ-MAHSHEV                PIC X(8).
          03 EZ-LO-TAKIN               PIC 9     VALUE ZERO.
          03 EZ-SOC-NAME-31            PIC X(31) JUST RIGHT.
          03 EZ-SOCHEN                 PIC 9(6).
          03 EZ-SUM-SOCHEN             PIC 9(6).
          03 EZ-RISHYONOT-20           PIC X(20).
          03 EZ-RISHYONOT-25           PIC X(20).
          03 EZ-ANAF                   PIC 999.
          03 EZ-QWRY                   PIC X     VALUE '0'.
          03 EZ-CHGA                   PIC X     VALUE '0'.
          03 EZ-AMLA                   PIC X     VALUE '0'.
          03 EZ-IMMD                   PIC X     VALUE '0'.
          03 EZ-CHSV                   PIC X     VALUE '0'.
          03 EZ-ISHR                   PIC X     VALUE '0'.
          03 EZ-PRMM                   PIC X     VALUE '0'.
          03 EZ-PRSH                   PIC X     VALUE '0'.
          03 EZ-DTA                    PIC X(1000).
          03 EZ-LDA.
             COPY DDS-ALL-FORMATS OF ELLDAARA.
          03 EZ-MAKOR                  PIC X VALUE '1'.
          03 EZ-SYS-C-O                PIC X(10) VALUE ' '.
          03 EZ-MHOZ-NUM               PIC X(2).
          03 EZ-UDATE                  PIC 9(8) VALUE 0.

ZMANI
ZMANI     03 EZ-ANAF-180               PIC 9 VALUE 0.
ZMANI        88 EZ-180-NO              VALUE 0.
ZMANI        88 EZ-180-YES             VALUE 1.

          03 EZ-X20-T            PIC X(20).
          03 EZ-X20-R REDEFINES EZ-X20-T.
             05 EZ-X10-T         PIC X(10).
             05 FILLER           PIC X(10).
          03 EZ-ISO1                   PIC X(47).
          03 EZ-CT10-KEY-122.
             05 EZ-CT10-T122-SOC     PIC 9(6).
             05 FILLER               PIC X(14).
          03 EZ-CT10-KEY-268.
             05 EZ-CT10-T268-SOC     PIC 9(6).
             05 EZ-CT10-268-REC      PIC X(12).
          03 EZ-CT10-106.
             05 EZ-KEY-TVL          PIC 9(3).
             05 EZ-KEY-FIL1         PIC 9(2).
             05 EZ-KEY-SOCEN        PIC 9(6).
             05 EZ-KEY-FIL2         PIC 9(9).
          03 EZ-KEY-155-129.
             05 EZ-NUM-TAT             PIC 9(3).
             05 EZ-ZERO                PIC X(5).
             05 EZ-129-ANAF            PIC 9(3).
ZMANI     03 EZ-KEY-155-200.
ZMANI        05 EZ-180-TAT             PIC 9(3).
ZMANI        05 EZ-180-ZERO            PIC 9(3).
ZMANI        05 EZ-180-ANAF            PIC 9(3).
ZMANI        05 EZ-180-NR              PIC X(2).
      *--ovride--
          03 EZ-MHOZ-OVR               PIC 99.
          03 EZ20-KOD-KEY.
             05  EZ20-COMP             PIC 999      VALUE 0.
             05  EZ20-MHOZ             PIC 99       VALUE 0.

       01 TVL70-REC.
          COPY DDS-ALL-FORMATS OF TVL070.
          66 T70ANF1  RENAMES T70F01 THRU T70R01.
          66 T70ANF2  RENAMES T70F02 THRU T70R02.
          66 T70ANF3  RENAMES T70F03 THRU T70R03.
          66 T70ANF4  RENAMES T70F04 THRU T70R04.
          66 T70ANF5  RENAMES T70F05 THRU T70R05.
          66 T70ANF6  RENAMES T70F06 THRU T70R06.
          66 T70ANF7  RENAMES T70F07 THRU T70R07.
          66 T70ANF8  RENAMES T70F08 THRU T70R08.
          66 T70ANF9  RENAMES T70F09 THRU T70R09.
          66 T70ANF10 RENAMES T70F10 THRU T70R10.

       01 TVL106-REC.
          COPY DDS-ALL-FORMATS OF TVL155R.
      *-------------*
      * SWXX-��÷��
      *-------------*
       01 SW00.
          03 SW-STATUS                 PIC XX  VALUE '00'.
          03 SW-LOOP                   PIC 9 VALUE 0.
             88 SW-END-LOOP      VALUE 9.
          03 SW-ANAF-FOUND             PIC 9 VALUE 0.
             88 SW-ANAF-FOUND-NO       VALUE 0.
             88 SW-ANAF-FOUND-YES      VALUE 1.
          03 SW-ANAF-FOUND-06          PIC 9 VALUE 0.
             88 SW-ANAF-FOUND-06-NO    VALUE 0.
             88 SW-ANAF-FOUND-06-YES   VALUE 1.
          03 SW-REC                    PIC 9 VALUE 0.
             88 SW-REC-END       VALUE 9.
          03 SW-TAVIM                  PIC 9 VALUE 0.
             88 SW-TAVIM-END     VALUE 9.
          03 SW-SYS                    PIC 9 VALUE 0.
             88 SW-SYS-CLOSE     VALUE 9.
          03 SW-KOD-HARIG              PIC X   VALUE '0'.
             88 SW-HARIG-TKINA                 VALUE '0'.
             88 SW-HARIG-SHG                   VALUE '1'.
          03 SW-SOCEN-RONBIT           PIC X   VALUE '0'.
             88 SW-SOCEN-RONBIT-NO             VALUE '0'.
             88 SW-SOCEN-RONBIT-YES            VALUE '1'.
          03 SW-KAYAM-ANAF             PIC X   VALUE '0'.
             88 KAYAM-ANAF-NO                  VALUE '0'.
             88 KAYAM-ANAF-YES                 VALUE '1'.
      *   03 SW-LOOP-TOKEF             PIC 9   VALUE 0.
      *      88 SW-END-LOOP-TOKEF   VALUE 1.
          03 SW-TOKEF                  PIC 9   VALUE 0.
             88 SW-TOKEF-END-LOOP              VALUE 1.
          03 SW-PAIL                   PIC 9   VALUE 0.
             88 SW-LO-PAIL                     VALUE 1.
gal   *               ���� �� - "1" ,���� - " " , ���� SAP ��� �������
gal    01 SW-SAP                       PIC X     VALUE ' '.
gal    01 SW_SAPSOC                    PIC X     VALUE ' '.
      *-------------*
      * PRMXX-�������
      *-------------*
       01 PR00.
      *---
          03 PR-VEC-ANAF-SHIDRUG.
             05 PR-VEC-ANAF OCCURS 50 INDEXED BY I-VEC.
                07 PR-ANAF-NO PIC 9(3).
      *---                                                     : INPUT
          03 PRFILE                    PIC X     VALUE ' '.
          03 PRUSER                    PIC X(10) VALUE ' '.
          03 PRZEVT                    PIC X(3)  VALUE ' '.
      *---                                                     :OUTPUT
          03 PRTVL.
             05 PRTVL-O OCCURS 500.
                07 PR1 PIC 9(6).
          03 PRTVP.
             05 PRTVP-O OCCURS 500.
                07 PR2 PIC 9(2).
      *---
          03 PR-ACC-CODE               PIC X(15) VALUE ' '.
      *---
 iso      03 PRM_USERNM         PIC X(10).
          03 PRM_MAHOZ          PIC 9(2).
          03 PRM_KPEUL          PIC 9(1).
      *     .2 = ������ �×�� �� ��÷� ���� ����� .1 = ���� �����
           88 PRM_USRSOC      VALUE 1.
           88 PRM_USRPKMS     VALUE 2.
          03 PRM_USERID         PIC 9(9).
          03 PRM_SOCNID         PIC 9(9).
          03 PRM_DTMSM          PIC 9(8).
          03 PRM_DTKMSM         PIC 9(8).
          03 PRM_TAFKID         PIC 9(1).
      *                          2 = ������ �×�� �� 1 = ��÷� ����
           88 PRM_PKDMIG      VALUE 1.
           88 PRM_MSTRO       VALUE 2.
          03 PRM_KODHZR         PIC 9.
           88 PRM_TAKIN       VALUE 0.
           88 PRM_SHGUY       VALUE 1.
           88 PRM_tokef       VALUE 2.
 iso      03 PRM_ERRMSG         PIC X(80).
          03 PRM_to_date        PIC 9(8).
 hzh      03  HZH-PARAM.
              05  HZH-USER              PIC X(10).
              05  HZH-DATE              PIC 9(8).
 hzh          05  HZH-KOD               PIC X(1).
 iso      03  iso-ISHUR-PARAM.
              05  iso-ISHUR-USER        PIC X(10).
              05  iso-SOCHEN-A          PIC 9(6) VALUE 999999.
              05  iso-SOCHEN-B          PIC 9(6) VALUE 999999.
              05  iso-KOLEKTIV          PIC 9(6) VALUE 0.
              05  iso-SUG-HESKEM        PIC 9(2) VALUE 17.
 iso          05  iso-ISHUR-KOD         PIC X VALUE '0'.
           03 ARS-ISHUR-PARAM.
              05 ARS-ISHUR-USER        PIC X(10).
              05 ARS-SOCHEN-A          PIC 9(6) VALUE 999999.
              05 ARS-SOCHEN-B          PIC 9(6) VALUE 999999.
              05 ARS-KOLEKTIV          PIC 9(6) VALUE 0.
              05 ARS-SUG-HESKEM        PIC 9(2) VALUE 17.
              05 ARS-ISHUR-KOD         PIC X VALUE '0'.
                 88  ARS-ISHUR-YES            VALUE '1'.
                 88  ARS-ISHUR-NO             VALUE '0'.
                 88  ARS-ISHUR-TVIOT          VALUE '2'.
      *-------------*
      * SHXX-�����
      *-------------*
      *---
           COPY ELCT050B  IN QCBLSRC.
      *----------------------------------M#AVL  - ������ ��×�� �×�� ���
       01  RTN-ELMAV-REC.
           COPY DDS-ALL-FORMATS OF ELMAVL.
 YDD  *---        ���� ���� ���×� ��� - WSOCHEN      ������� ���
          03 PRM-WSOC.
             05 EZ-HVR                 PIC 9(1) VALUE 0.
             05 EZ-SOC                 PIC 9(6) VALUE 0.
             05 EZ-MAH                 PIC 9(2) VALUE 0.
             05 EZ-MAT                 PIC 9(2) VALUE 0.
             05 EZ-STATUS              PIC 9(1) VALUE 0.
       01 DATA-AREA1.
           COPY DDS-ALL-FORMATS OF ELDTAARA.

 YDD  *---                  ��� ����� - ELNT020R     ������� ���
       01  LN020-PARM.
           03 LN020-MAHOZ                 PIC 9(02).
           03 LN020-SOXEN                 PIC 9(06).
           03 LN020-DATE                  PIC 9(08).
           03 LN020-USER                  PIC X(10).
           03 LN020-USNM                  PIC X(30).

      *---                       TVLKLLL1 ������ - ELTV010B ������� ���
           COPY ELCT010B IN QCBLSRC.
      *---                       155 ��×� ������ - ELTV010B ������� ���
ZMANI      COPY ELCT155B IN QCBLSRC.
      *--------------------------TVLKODL1 ����� - ELTV020B  ������� ���
           COPY ELCT020B  IN QCBLSRC.
       01 TVLKOD-REC.
           COPY DDS-ALL-FORMATS   OF TVLKODL1.
      *-------------------ELTV010B ��×�� ����� ������� - ����� ��×� ���
           03 RTN-TVL-DATA.
              04 RTN-TVL-REC OCCURS 25 TIMES.
                 COPY DDS-ALL-FORMATS OF TVLKLLL1.

       01 TVL00-REC.
           COPY DDS-ALL-FORMATS  OF TVL000.
       01 TVL06-REC.
           COPY DDS-ALL-FORMATS  OF TVL006.
       01  iso-DATA-AREA.
           COPY DDS-ALL-FORMATS OF isoDTAARA.

      *-------------*
      * IXX-�������
      *-------------*
       01 I00.
          03 I                         PIC 9(4) VALUE 0.
          03 I-ANF-S                   PIC 9(3) VALUE 0.
          03 J                         PIC 9(4) COMP-3.
          03 I-ERR                     PIC 9(2) VALUE 0.
          03 I-SYS-C                   PIC 9    VALUE 0.
          03 I-ANAF                    PIC 9(2) VALUE 0.
 YDD      03 I-SOC                     PIC 9(4) VALUE 0.
          03 I-RIS                     PIC 9(2) VALUE 0.
          03 I-TAV                     PIC 9(2) VALUE 0.
      *-------------*
      * TVXX-����×�
      *-------------*
       01 TV00.
          03 TVL-CHGA.
             05 TV-CHGA-O OCCURS 10 INDEXED BY I-CHGA.
                07 TV-ANAF70 PIC 9(3) COMP-3.
                07 TV-ARSHAA PIC 9(1).
          03 TV-SOCHNIM.
             05 TV-SOCHNIM-O OCCURS 500 INDEXED BY I-TV-SOC.
                07 TV-SOCHEN PIC 9(6).
                07 TV-SOCHEN-NAME    PIC X(30).
                07 TV-SOCHEN-TZ      PIC 9(9).
                07 TV-SOCHEN-ADDRS   PIC X(30).
                07 TV-SOCHEN-TEL     PIC 9(9).
                07 TV-SOCHEN-MAIL    PIC X(40).
          03 TV-ANAFIM.
             05 TV-ANAFIM-O OCCURS 50.
                07 TV-ANAF   PIC 9(3).
          03 TV-SYSTEM.
             05 TV-SYSTEM-O OCCURS 10 INDEXED BY I-I.
                07 TV-SYS-MHOZ  PIC 99 VALUE 99.
                07 TV-SYS-MAZAV PIC X  VALUE ' '.
          03 TV-RISHYONOT.
             05 TV-RISHYONOT-O OCCURS 10.
                07 TV-RISHAYON PIC 999 COMP-3.

      *-------------*
      * MMXX-�����
      *-------------*
      *01 MM00.

       01  PI3  PIC X.
      *-----------------------------------------------------------------
       LINKAGE     SECTION.
      *-----------------------------------------------------------------
       01  PI1.
           COPY PRM1I IN QCBLSRC.
       01  PO2.
           COPY PRM2O IN QCBLSRC.
       01  PO4.
           COPY PRMUSER4O IN QCBLSRC.

      *=================================================================
       PROCEDURE DIVISION USING PI1 PO2 PO4.
      *=================================================================
       MAIN SECTION.
      *-----------------------------------------------------------------
       000.
           PERFORM 001-SET-ERROR-HANDLER.
           PERFORM H-HATCHALA.
           IF PO2_ESKERR = 1
              GO TO 050.

           PERFORM 100-ESSUGU.
           IF PO2_ESKERR = 1
              GO TO 050.

           PERFORM 200-ESMHOZ.
           IF PO2_ESKERR = 1
              GO TO 050.

           PERFORM 300-ESMHZN.
           IF SW-SYS-CLOSE
              GO TO 050.

           PERFORM BNIA-LDA.
           IF PO2_ESKERR = 1
              GO TO 050.

           PERFORM BNIA-DTA.

           PERFORM 400-ESSDLT-ESSMKS.

           PERFORM 500-ESSOCN.
           IF PO2_ESKERR = 1
              GO TO 050.

           IF DASUGU = '1'
              PERFORM 550-KRIAT-TAVLAOT
              IF PO2_ESKERR = 1
                 GO TO 050
              END-IF
           END-IF

           PERFORM 600-ANAFIM.
           IF PO2_ESKERR = 1
              GO TO 050.

           PERFORM 800-PRATIM-NOSAFIM.
           IF PO2_ESKERR = 1
              GO TO 050.

           PERFORM 900-HARSHAOT-IMAGE.
      *---                               ���� - 2 , ��� - 1 - ����� ÷��
           COMPUTE ES_SUG_USER  = ES_SUG_USER + 1.
       050.
           IF PO2_ESKERR = 1
              INITIALIZE PO4.

           PERFORM S-SOF
           GOBACK.
       000-EXIT.
           EXIT.
      *-----------------------------------------------------------------
       001-SET-ERROR-HANDLER   SECTION.
      *-----------------------------------------------------------------
       000.
            MOVE 16 TO BYTES-PROVIDED OF QUS-EC.
            SET ERROR-HANDLER TO ENTRY LINKAGE PROGRAM 'ESERHLEB'
            MOVE ZERO  TO BYTES-AVAILABLE OF QUS-EC.
            CALL 'QlnSetCobolErrorHandler' USING ERROR-HANDLER
                                                 OLD-ERROR-HANDLER
                                                 QUS-EC.
  *******  IF BYTES-AVAILABLE OF QUS-EC > 0
  *******     DISPLAY 'Error setting handler'
  ********    STOP RUN.
       999.     EXIT.
      *-----------------------------------------------------------------
       H-HATCHALA      SECTION.
      *-----------------------------------------------------------------
       H00.
      *---                                ������ ����� ������ LDA �����
           CALL 'ESSTRSRVB'.

      *---                                   �×��� INPUT ������� �×���
           MOVE 2003 TO PI1_ESTASK.
           CALL 'ESPRMIB' USING PI1 PI3 PO2.

           INITIALIZE PO2.
           INITIALIZE PO4.
           MOVE '0' TO ES_ISOH_BDIKA.
           PERFORM H100 VARYING I FROM 1 BY 1                             4118
                       UNTIL I > 50.
           GO TO H200.
       H100.
               MOVE '0' TO     ES_QWRY(I)
                               ES_CHGA(I)
                               ES_IMMD(I)
                               ES_CHSV(I)
                               ES_ISHR(I)
                               ES_AMLA_PREMIUM_UPDATE_B(I)
                               ES_AMLA_ARSHAA_READ_A(I)
                               ES_AMLA_ARSHAA_UPDATE_A(I)
                               ES_AMLA_ARSHAA_READ_B(I)
                               ES_AMLA_ARSHAA_UPDATE_B(I)
                               ES_MISHNE_UPDATE_B(I)
                               ES_MESHUTAF_UPDATE_B(I)
                               ES_MASAX_PREMIUMIM(I)
                               ES_MASAX_SHEABUD(I).

       H200.
           IF PI1_ESUSER = 'INTER104  '
              ADD  1                           TO I-ERR
              MOVE 0001                        TO PO2_ESERKD(I-ERR)
              MOVE '!�� �×�×�× ��×�� ����� ����' TO PO2_ESERTX(I-ERR)
              MOVE 1                           TO PO2_ESERSG(I-ERR)
              MOVE 1                           TO PO2_ESKERR
              ADD  1                           TO PO2_ESKERR_NR
              GO TO H-EXIT
           END-IF.
 *******            ���� - 155/129 ��×� ��� 180 ��� �� ��×�� ������
ZMANI      SET EZ-180-NO TO TRUE.
ZMANI      PERFORM 155-200-ZMANI.
           OPEN INPUT USERSS1.
           OPEN INPUT ELNTUSR
                      USERSANF
                      RSOCENF
                      ELMAVL
                      ESMAVL.

           INITIALIZE PR00
                      R2MAV OF RTN-ELMAV-REC.

           MOVE 0 TO I-ERR.
           MOVE 0 TO PO2_ESKERR_NR.
           MOVE 0 TO I.
           MOVE 0 TO SW-LOOP.
           MOVE 0 TO SW-SYS.
           MOVE ZERO  TO ES_DMAI_POLISA_TOS_INF.

      *---                            isodtaara       ������� CL ������
           CALL 'ELIDTA' USING iso-data-area.
      *---                            ELDTACKSAP      ������� CL ������
           CALL 'ELDTASAPC' USING SW-SAP.
      *---              USERPROFILE-� ACCOUNTING CODE ������� CL ������
           CALL 'ESUSERC' USING PI1_ESUSER PR-ACC-CODE EZ-ERR.
           IF EZ-ERR = 1
              ADD  1        TO I-ERR
              MOVE 0007     TO PO2_ESERKD(I-ERR)
              MOVE 1    TO PO2_ESERSG(I-ERR)
              MOVE 1    TO PO2_ESKERR
              ADD  1        TO PO2_ESKERR_NR
              GO TO H-EXIT.

      *---                                          OVERIDE-� ���� ��×�
           PERFORM 901-MHOZ.

      *---             "���÷� �����" ��� ����� (CTLEXE) DTA ����� �����
           ACCEPT EZ-DTA FROM DATA-AREA FOR "CTLEXE" LIBRARY "FILMNG".

           ACCEPT EZ-UDATE FROM DATE.
           ADD 20000000 TO EZ-UDATE.

       H-EXIT.
           EXIT.
      *-----------------------------------------------------------------
       100-ESSUGU      SECTION.
      *-----------------------------------------------------------------
       100.
      *---����� ���---

      *---����---
           IF PR-ACC-CODE(5:2) = 'SC'
              MOVE 1 TO ES_SUG_USER
              GO TO 100-EXIT
           END-IF.

      *---���---
           IF PR-ACC-CODE(5:2) = 'HF' OR
              PR-ACC-CODE(5:2) = 'TV' OR
              PR-ACC-CODE(5:2) = 'OP'
              MOVE 0 TO ES_SUG_USER
              GO TO 100-EXIT
           END-IF.

      *---�����---
           IF PR-ACC-CODE = 'QPGMR'
              MOVE 0 TO ES_SUG_USER
              GO TO 100-EXIT
           END-IF.

      *---���---
           ADD  1        TO I-ERR.
           MOVE 0008     TO PO2_ESERKD(I-ERR).
           MOVE 'ESSUGU' TO PO2_ESFLDN(I-ERR).
           MOVE 1    TO PO2_ESKERR
           MOVE 1    TO PO2_ESERSG(I-ERR).
           ADD  1        TO PO2_ESKERR_NR.

       100-EXIT.
           EXIT.
      *-----------------------------------------------------------------
       200-ESMHOZ      SECTION.
      *-----------------------------------------------------------------
       200.
      *---���×� ����---

           IF PI1_ESMHOZ  NOT = 0
              MOVE PI1_ESMHOZ  TO ES_MHOZ
              MOVE PI1_ESMHOZ  TO EZ-MHOZ-NUM
              GO TO 200-EXIT.
      *---��� ��� ���� �� ������ ��---
           IF PR-ACC-CODE = 'QPGMR'
              MOVE 04 TO ES_MHOZ
              MOVE 04 TO EZ-MHOZ-NUM
              GO TO 200-EXIT.

      *---                   ������� ��� ACC-× 4-� 3 �������� ��� ����×
           MOVE PR-ACC-CODE(3:2) TO EZ-MHOZ-NUM.
           IF EZ-MHOZ-NUM IS NUMERIC
              MOVE PR-ACC-CODE(3:2) TO ES_MHOZ
           ELSE
              ADD  1        TO I-ERR
              MOVE 0005     TO PO2_ESERKD(I-ERR)
              MOVE 'ESMHOZ' TO PO2_ESFLDN(I-ERR)
              MOVE 1    TO PO2_ESERSG(I-ERR)
              MOVE 1    TO PO2_ESKERR
              ADD  1        TO PO2_ESKERR_NR
           END-IF.
      *--                      ACCOUNTING CODE -� ����� ���� .��
           IF ES_MHOZ NOT = 0
              MOVE ES_MHOZ TO EZ-MHOZ-NUM.
       200-EXIT.
           EXIT.
      *-----------------------------------------------------------------
       300-ESMHZN      SECTION.
      *-----------------------------------------------------------------
       300.
      *---������ ������� ������ 5 ��---
           MOVE PI1_ESUSER TO USSNUM OF USERSS1-REC.
           MOVE 0          TO USSHEV OF USERSS1-REC.
           START USERSS1
                 KEY NOT LESS EXTERNALLY-DESCRIBED-KEY
                 INVALID KEY
                         ADD 1         TO I-ERR
                         MOVE 0001     TO PO2_ESERKD(I-ERR)
                         MOVE 'ESMHZN' TO PO2_ESFLDN(I-ERR)
                         MOVE 1        TO PO2_ESERSG(I-ERR)
                         MOVE 1        TO PO2_ESKERR
                         ADD 1         TO PO2_ESKERR_NR
                         GO TO 300-EXIT.


           MOVE 0 TO SW-LOOP.
           MOVE 0 TO I.
           PERFORM 310 THRU 310-CONT UNTIL SW-END-LOOP.
           GO TO 320.
       310.
           ADD 1 TO I.
           IF I = 6
              SET SW-END-LOOP TO TRUE
              GO TO 310-CONT.

           READ USERSS1 NEXT
                     AT END
                     SET SW-END-LOOP TO TRUE
                     GO TO 310-CONT.
           IF USSNUM OF USERSS1-REC NOT = PI1_ESUSER
              SET SW-END-LOOP TO TRUE
              GO TO 310-CONT.
  *****            ����� ���� �� ��� - �×��  ���� ���� �� ������ ��
  *****    IF USHARH NOT = '�'
  *****       SET SW-END-LOOP TO TRUE
  *****       GO TO 310-CONT.
eliahu     IF USSHEV OF USERSS1-REC = 80 AND ES_SUG_USER = 1
              GO TO 310-CONT.
           MOVE USSHEV OF USERSS1-REC TO ES_MHZN(I).
       310-CONT.
       320.

      *---                                           ������ ������ �"��
           COMPUTE ES_SUM_MHOZ = I - 1.

      *---          (OPEN=O ,CLOSE=C)��=1 , ��=0 :���� ���× ���÷� �����
           PERFORM 350-SYSTEM-C-O.
       300-EXIT.
           EXIT.
      *-----------------------------------------------------------------
       350-SYSTEM-C-O        SECTION.
      *-----------------------------------------------------------------
       350.
      *---��=1,��=0:���� ���× ���÷� �����---
      *---                                                      ���� ��
           IF PR-ACC-CODE(5:2) = 'SC'
              IF EZ-DTA(201:1) = 'C'
                 SET SW-SYS-CLOSE TO TRUE
                 MOVE 1 TO ES_SYS_OPEN_CLOSE(1)
                 ADD  1        TO I-ERR
                 MOVE 0004     TO PO2_ESERKD(I-ERR)
                 MOVE 1        TO PO2_ESERSG(I-ERR)
                 MOVE 1        TO PO2_ESKERR
                 ADD  1        TO PO2_ESKERR_NR
              ELSE
                 MOVE 0 TO ES_SYS_OPEN_CLOSE(1)
              END-IF
              GO TO 350-EXIT
           END-IF.

      *---                                                ��� �� ��� ��
      ***  IF PR-ACC-CODE(5:2) = 'HF'
      *---       DTA-� -(��÷�/����)���� ����:���� 2 �� �� �� ��×� �����
           MOVE 0  TO TV-SYS-MHOZ(1).
           MOVE EZ-DTA(1:1) TO TV-SYS-MAZAV(1).
           MOVE 2  TO TV-SYS-MHOZ(2).
           MOVE EZ-DTA(2:1) TO TV-SYS-MAZAV(2).
           MOVE 3  TO TV-SYS-MHOZ(3).
           MOVE EZ-DTA(3:1) TO TV-SYS-MAZAV(3).
           MOVE 4  TO TV-SYS-MHOZ(4).
           MOVE EZ-DTA(4:1) TO TV-SYS-MAZAV(4).
           MOVE 6  TO TV-SYS-MHOZ(5).
           MOVE EZ-DTA(5:1) TO TV-SYS-MAZAV(5).
           MOVE 7  TO TV-SYS-MHOZ(6).
           MOVE EZ-DTA(6:1) TO TV-SYS-MAZAV(6).
           MOVE 8  TO TV-SYS-MHOZ(7).
           MOVE EZ-DTA(7:1) TO TV-SYS-MAZAV(7).
           MOVE 80 TO TV-SYS-MHOZ(8).
           MOVE EZ-DTA(8:1) TO TV-SYS-MAZAV(8).


           MOVE 0 TO SW-LOOP.
           MOVE 0 TO I-SYS-C.
           MOVE 1 TO I.
           SET I-I TO 1.

      *---    SYS-� ��×�× ���� ��� �� (5 ��)������ ��×�� ���� ��� �����
           PERFORM 355 THRU 355-CONT UNTIL SW-END-LOOP.
           GO TO 356.
       355.
           SEARCH TV-SYSTEM-O
                  AT END
                     SET SW-END-LOOP TO TRUE
                     GO TO 355-CONT
                  WHEN TV-SYS-MHOZ(I-I) = ES_MHZN(I)
                   IF TV-SYS-MAZAV(I) = 'C'
                      MOVE 1 TO ES_SYS_OPEN_CLOSE(I)
                      ADD  1    TO I-ERR
                      MOVE 0004 TO PO2_ESERKD(I-ERR)
                      MOVE 1    TO PO2_ESERSG(I-ERR)
                      MOVE 1    TO PO2_ESKERR
                      ADD  1    TO PO2_ESKERR_NR
                      MOVE '! ���×�� ���÷� ������' TO PO2_ESERTX(I-ERR)
                      ADD 1 TO I-SYS-C
                   ELSE
                      MOVE 0 TO ES_SYS_OPEN_CLOSE(I)
                   END-IF.
           IF I = 5 OR I = ES_SUM_MHOZ
              SET SW-END-LOOP TO TRUE
              GO TO 355-CONT.

           ADD 1 TO I.
           SET I-I UP BY 1.
       355-CONT.
       356.
      *---                         ���� <-- ���÷� ������ ������� ��× ��
           IF I-SYS-C = ES_SUM_MHOZ
              SET SW-SYS-CLOSE TO TRUE
              GO TO 350-EXIT.

       350-EXIT.
           EXIT.
      *-----------------------------------------------------------------
       400-ESSDLT-ESSMKS     SECTION.
      *-----------------------------------------------------------------
       400.
      *---                                     ���� ���� ����� (����)��
      *---                 !���×�� ���� ����× USERSS1 �×��� ������ ��×�
           MOVE PI1_ESUSER TO USSNUM OF USERSS1-REC.
           MOVE ES_MHOZ    TO USSHEV OF USERSS1-REC.
           READ USERSS1
                INVALID KEY
                        ADD  1        TO I-ERR
                        MOVE 0002     TO PO2_ESERKD(I-ERR)
                        MOVE 1        TO PO2_ESERSG(I-ERR)
                        MOVE 1        TO PO2_ESKERR
                        ADD  1        TO PO2_ESKERR_NR
                        GO TO 400-EXIT.
      *-- !!!���÷� ����� �� �� ,����, ����� ���� �� ������ �� ����×
           IF USSNUM OF USERSS1-REC NOT =
              PI1_ESUSER
              OR USSHEV OF USERSS1-REC NOT =
              ES_MHOZ
              ADD  1    TO I-ERR
              MOVE 400  TO PO2_ESERKD(I-ERR)
              MOVE 1    TO PO2_ESERSG(I-ERR)
              MOVE 1    TO PO2_ESKERR
              ADD  1    TO PO2_ESKERR_NR
              MOVE '! �� ����× ���×�� ���� ����'
              TO PO2_ESERTX(I-ERR)
              GO TO 400-EXIT
           END-IF

      ***  MOVE USSDLT OF USERSS1-REC(16:10) TO ES_USER_NAME.
           MOVE USSNUM OF USERSS1-REC TO ES_USER_NAME.
           MOVE USSMKS OF USERSS1-REC TO ES_KOD_MAKISH.

      *--            (650-PRATEI-ANFIM  SECTION)������ ��×�× ���� �����
           MOVE USQWRY OF USERSS1-REC TO EZ-QWRY.
           MOVE USCHGA OF USERSS1-REC TO EZ-CHGA.
           MOVE USAMLA OF USERSS1-REC TO EZ-AMLA.
           MOVE USIMMD OF USERSS1-REC TO EZ-IMMD.
           MOVE USCHSV OF USERSS1-REC TO EZ-CHSV.
           MOVE USISHR OF USERSS1-REC TO EZ-ISHR.
           MOVE USPRMM OF USERSS1-REC TO EZ-PRMM.
           MOVE '�'                   TO EZ-PRSH.

       400-ARSH-DFT.
      *-        ���� ����� USERSANF-× ��÷�� ��� ���� ������� ������ ���
      *--                        !!! USERS -� ������ ���� ������ ������
           MOVE '�' TO EZ-QWRY.
      *--  MOVE '�' TO EZ-CHGA.
      *--  MOVE '�' TO EZ-AMLA.
           MOVE '�' TO EZ-IMMD.
           MOVE '�' TO EZ-CHSV.
           MOVE '�' TO EZ-ISHR.
       400-EXIT.
           EXIT.
      *-----------------------------------------------------------------
       500-ESSOCN        SECTION.
      *-----------------------------------------------------------------
       500.
      *---                    (MAX 200)������ ������� ������ ��×�� ����
           MOVE '1' TO PRFILE.
           MOVE PI1_ESUSER TO PRUSER.
           CALL 'ELNT010R' USING PRFILE
                                 PRUSER
                                 PRZEVT
                                 PRTVL
                                 PRTVP.

      *---                20 ���� ������� ����� �÷�� ��×�� 6 ��×�� ����
           MOVE 20 TO EZ-ANAF.
           PERFORM 506-KRIA-TVL006.
           IF SW-ANAF-FOUND-06-NO
              GO TO 500-EXIT.
           MOVE T06STA TO EZ-RISHYONOT-20.
      *---                25 ���� ������� ����� �÷�� ��×�� 6 ��×�� ����
           MOVE 25 TO EZ-ANAF.
           PERFORM 506-KRIA-TVL006.
           IF SW-ANAF-FOUND-06-NO
              GO TO 500-EXIT.
           MOVE T06STA TO EZ-RISHYONOT-25.

      *---      (�����) ���� ����� ���� ���/��� �� ����� ����� �� ��×��
      *---    ���� ��� ��×� ��� ��� �� ,����� ��� ��×� ,����� ��� �� ��
 YDD       IF ES_SUG_USER = 1
  |           INITIALIZE LN020-PARM
  |           MOVE EZ-MHOZ-NUM TO LN020-MAHOZ
  |           MOVE PR1(1)      TO LN020-SOXEN
  |           MOVE 99999999    TO LN020-DATE
  |           CALL 'ELNT020R' USING LN020-PARM
  |           MOVE LN020-USER TO ES_USER_HATAM
  |           MOVE LN020-USNM TO ES_NAME_HATAM
  |        ELSE
  |           MOVE ES_USER_NAME          TO ES_USER_HATAM
  |  ?        MOVE USSDLT OF USERSS1-REC TO ES_NAME_HATAM
 YDD       END-IF.

      *---         !!!������� �� �� ��×�� �� <-- 999999 = ����� ���� ��
      *---                 ���������� ����� ������� �� ���×� �� �� ����
           IF PR1(1) = 999999
              MOVE PR1(1) TO ES_SOCHEN(1)
              MOVE 1      TO ES_SUM_SOCHEN
              GO TO 500-EXIT.
      *    IF PR1(1) = 999999
      *       PERFORM M-MILUY-ALL-SOC
      *       IF PO2_ESKERR = 1
      *          GO TO 500-EXIT
      *       ELSE
      *          GO TO 555
      *       END-IF
      *    END-IF.
      *---                     .����× ��������� ��� ���� ����� �� ����×
      *---                                 .999999-� ���� ����� ���� ��
           MOVE 1 TO I-SOC.
           PERFORM 520-SOCEN-PAIL UNTIL I-SOC > 500    OR
                                        PR1(I-SOC) = 0 OR
                                        PO2_ESKERR = 1.
           IF PO2_ESKERR = 1
              GO TO 500-EXIT.

       555.
      *---       (�����)��×� �����× PRTVL-× ������ ��� ���� ��×�� ��×��
      *---              (���� ����=������)�������� ������� 500 ��-��×÷�

           MOVE 0 TO I.
           MOVE 0 TO SW-LOOP.
           MOVE 0 TO I-SOC.
           PERFORM 510 THRU 510-CONT UNTIL SW-END-LOOP.
           GO TO 520.
       510.
           ADD 1 TO I.
           IF I > 500
              SET SW-END-LOOP TO TRUE
              GO TO 510-CONT.
      *---               ����× ��/������ �� �� �� - ������ ������ �����
           IF PR1(I) = 0
              GO TO 510-CONT.

           ADD 1 TO I-SOC.
           MOVE PR1(I) TO ES_SOCHEN(I-SOC).

      *---      ���� �� '× �� '� ����,���� ,����� ,�×��� ,�� :���� ����
           PERFORM 540-PRATIM-SOCHEN.

       510-CONT.
       520.
      *---                                           ������ ������ �"��
      *    COMPUTE ES_SUM_SOCHEN = I - 1.
           MOVE I-SOC TO ES_SUM_SOCHEN.

           IF ES_SUM_SOCHEN = 0
              ADD  1                           TO I-ERR
              MOVE 0006                        TO PO2_ESERKD(I-ERR)
              MOVE '!������ ������ ������ ���' TO PO2_ESERTX(I-ERR)
              MOVE 1                           TO PO2_ESERSG(I-ERR)
              MOVE 1                           TO PO2_ESKERR
              ADD  1                           TO PO2_ESKERR_NR
              GO TO 500-EXIT.
       500-EXIT.
           EXIT.
      *-----------------------------------------------------------------
       M-MILUY-ALL-SOC   SECTION.
      *-----------------------------------------------------------------
       M00.
      *---������ ������� ��× ��×�� �� ���� �� <-- 999999=������ �������

           MOVE PI1_ESMHOZ TO M2HVRA OF ELMAVL-REC.
           MOVE 0          TO M2HESH OF ELMAVL-REC.
           MOVE 0          TO M2MTBA OF ELMAVL-REC.
           START ELMAVL
              KEY NOT LESS EXTERNALLY-DESCRIBED-KEY
              INVALID KEY
                 ADD 1                        TO I-ERR
                 MOVE 0008                    TO PO2_ESERKD (I-ERR)
                 MOVE 1                       TO PO2_ESERSG (I-ERR)
                 MOVE '!ELMAVL-� ����× ���÷�' TO PO2_ESERTX(I-ERR)
                 MOVE 1                       TO PO2_ESKERR
                 ADD 1                        TO PO2_ESKERR_NR
                 GO TO M-EXIT.

           MOVE 0 TO SW-REC.
           MOVE 1 TO I-SOC.
           PERFORM M10 THRU M10-CONT UNTIL I-SOC > 201    OR
                                           PO2_ESKERR = 1 OR
                                           SW-REC-END.
           GO TO M20.
       M10.
           READ ELMAVL NEXT
                AT END
                   SET SW-REC-END TO TRUE
                   GO TO M10-CONT.
           IF M2HVRA OF ELMAVL-REC NOT= PI1_ESMHOZ
              SET SW-REC-END TO TRUE
              GO TO M10-CONT.
           IF M2MTBA OF ELMAVL-REC NOT= 1
              GO TO M10-CONT.

           MOVE M2HESH OF ELMAVL-REC TO PR1(I-SOC).
      *---                                        ��/�� ���� ���� ����×
           IF M2STCR OF ELMAVL-REC NOT= 0
              INITIALIZE PR1(I-SOC)
              GO TO MNEXT.
      *---                                           ������� ���� ����×
           PERFORM 530-TOKEF-SOCEN.
           IF PO2_ESKERR = 1
              GO TO M10-CONT.
      *---                  (����×/���� �� ��)����� �� ��� ��-���� ����
       MNEXT.
           IF PR1(I-SOC) NOT= 0
              ADD 1 TO I-SOC.
      *---
       M10-CONT.
       M20.

           IF PO2_ESKERR = 1
              GO TO M-EXIT.

       M-EXIT.
           EXIT.
      *-----------------------------------------------------------------
       520-SOCEN-PAIL   SECTION.
      *-----------------------------------------------------------------
       520.
      *---                                          ���� ����� �� ����×
           MOVE ES_MHOZ    TO M2HVRA OF ELMAVL-REC
           MOVE PR1(I-SOC) TO M2HESH OF ELMAVL-REC
           MOVE 1          TO M2MTBA OF ELMAVL-REC
           READ ELMAVL
                INVALID KEY
                   INITIALIZE PR1(I-SOC)
                   GO TO T520-NEXT.
           IF M2STCR OF ELMAVL-REC NOT= 0
              INITIALIZE PR1(I-SOC)
              GO TO T520-NEXT.

           PERFORM 530-TOKEF-SOCEN.
           IF PO2_ESKERR = 1
              GO TO 520-EXIT.

       T520-NEXT.
           ADD 1 TO I-SOC.

       520-EXIT.
           EXIT.
      *-----------------------------------------------------------------
       530-TOKEF-SOCEN   SECTION.
      *-----------------------------------------------------------------
       530.
      *---                                    .����� ������� ���� ����×

      *--- (0-������,����� 5:����).�� �� ����� ���� ���×� ���� �� ����×
           MOVE 1             TO EZ-HVR
           MOVE PR1(I-SOC)    TO EZ-SOC
           MOVE ES_MHOZ       TO EZ-MAH
           MOVE 1             TO EZ-MAT
           MOVE 0             TO EZ-STATUS
           CALL 'WSOCHEN' USING PRM-WSOC.
      *---                          ���×� �� = 1,���×� = 0 :����� �����
           IF EZ-STATUS = 1
              GO TO 530-EXIT.

      *---                       (�������� �� �� ���×) ����� ���� ����×
      *---                    .����× �� ����� ,����× �� �������� ��� ��

      *---                        25:���× ����� �÷�� ��×�� 6 ��×� �����
           MOVE EZ-RISHYONOT-25 TO TV-RISHYONOT.

           MOVE 0 TO SW-TOKEF.
           MOVE 0 TO I-RIS.
           PERFORM 535-CHECK-R UNTIL SW-TOKEF-END-LOOP.
           IF PO2_ESKERR = 1
              GO TO 530-EXIT.
      *---   ����×× ���� ���-(����× ��/���� �� �×� ��� ��)���� ����� ��
      *---                                      ��� ���× ������ �������
           IF PR1(I-SOC) = 0
              GO TO 530-EXIT.

      *---                        20:���× ����� �÷�� ��×�� 6 ��×� �����
           MOVE EZ-RISHYONOT-20 TO TV-RISHYONOT.

           MOVE 0 TO SW-TOKEF.
           MOVE 0 TO I-RIS.
           PERFORM 535-CHECK-R UNTIL SW-TOKEF-END-LOOP.
           IF PO2_ESKERR = 1
              GO TO 530-EXIT.
      *---   ����×× ���� ���-(����× ��/���� �� �×� ��� ��)���� ����� ��
      *---                                      ��� ���× ������ �������
           IF PR1(I-SOC) = 0
              GO TO 530-EXIT.

       530-EXIT.
           EXIT.
      *-----------------------------------------------------------------
       535-CHECK-R           SECTION.
      *-----------------------------------------------------------------
       535.
      *---  (����× �����)���� �� �� 25-� 20 ���� �������� �÷�� �� ����×

           ADD 1 TO I-RIS.
           IF TV-RISHAYON(I-RIS) = 0
              SET SW-TOKEF-END-LOOP TO TRUE
              GO TO 535-EXIT.

           MOVE ES_MHOZ            TO LCMHOZ OF RSOCEN-REC.
           COMPUTE LCSOCN OF RSOCEN-REC = (PR1(I-SOC) / 10) * 10.
           MOVE 1                  TO LCTCUM OF RSOCEN-REC.
           MOVE TV-RISHAYON(I-RIS) TO LCSUGR OF RSOCEN-REC.
           READ RSOCENF
              INVALID KEY
              INITIALIZE PR1(I-SOC)
              SET SW-TOKEF-END-LOOP TO TRUE
              GO TO 535-EXIT.

           IF LCTOKF OF RSOCEN-REC < EZ-UDATE
              INITIALIZE PR1(I-SOC)
              SET SW-TOKEF-END-LOOP TO TRUE
              GO TO 535-EXIT.

       535-EXIT.
           EXIT.
      *-----------------------------------------------------------------
       506-KRIA-TVL006       SECTION.
      *-----------------------------------------------------------------
       506.
               SET SW-ANAF-FOUND-06-YES TO TRUE.
      *---                                        006  ����� ��×� �����
           MOVE '0'                  TO CT10-KOD-HEMSH.
           MOVE 06                   TO CT10-TVL-NO.
           MOVE PI1_ESCOMP           TO CT10-HEVRA.
           MOVE PI1_ESMHOZ           TO CT10-MAHOZ.
           MOVE EZ-ANAF              TO CT10-ANAF.
     ***** MOVE 0                    TO CT10-MATBEA.
           MOVE 1                    TO CT10-MATBEA.
           MOVE SPACES               TO CT10-KEY-KLLI.
           MOVE 99999999             TO CT10-DATE.
           MOVE ZERO                 TO CT10-NO-SDRI.
           SET  CT10-OVERIDE-NO      TO TRUE.
      **-  SET  CT10-OVERIDE-YES     TO TRUE.
           MOVE EZ-MHOZ-OVR          TO CT10-IND-OVERIDE.
           SET  CT10-REC-SINGL       TO TRUE.
           CALL 'ELTV010B'  USING CT10-AREA-KLALI RTN-TVL-DATA.
           IF NOT CT10-HZR-TAKIN
              THEN
               GO TO 506-EXIT
               ADD  1  TO I-ERR
               MOVE 06 TO PO2_ESERKD(I-ERR)
               MOVE 1  TO PO2_ESERSG(I-ERR)
     ******    MOVE '6 ��×�' TO PO2_ESFLDN(I-ERR)
               MOVE EZ-ANAF  TO PO2_ESFLDN(I-ERR)
               MOVE '6 ��×�× ��÷�� �� ���' TO PO2_ESERTX(I-ERR)
               MOVE 0  TO PO2_ESKERR
               ADD  1  TO PO2_ESKERR_NR
               SET SW-ANAF-FOUND-06-NO TO TRUE
               GO TO 506-EXIT
           END-IF.
           MOVE TUREC OF RTN-TVL-REC (1) TO TVL06-REC.
       506-EXIT.
           EXIT.
      *-----------------------------------------------------------------
       550-KRIAT-TAVLAOT SECTION.
      *-----------------------------------------------------------------
       550.
           PERFORM 551-READ-TVL70
           IF PO2_ESKERR = 1
              GO TO 550-EXIT
           END-IF
           PERFORM 552-SOCEN-RONBIT.
       550-EXIT.
           EXIT.
      *-----------------------------------------------------------------
       600-ANAFIM        SECTION.
      *-----------------------------------------------------------------
       600.
      *---                     (MAX 10)������ �������  ����� ��×�� ����
           MOVE '0' TO ES_ANAF_999.
           INITIALIZE PRZEVT PRTVL PRTVP.
           MOVE '2' TO PRFILE.
           MOVE PI1_ESUSER TO PRUSER.
           CALL 'ELNT010R' USING PRFILE
                                 PRUSER
                                 PRZEVT
                                 PRTVL
                                 PRTVP.

    *****                                         ��×�� ���� ����� ��
           IF PR1(1) = 999
             PERFORM 620-ANAFIM-SHIDRUG
             IF PO2_ESKERR = 1
                GO TO 600-EXIT
             END-IF
             MOVE I-ANAF TO  ES_SUM_ANAF
             GO TO 630
           END-IF.

    *****                                            ��×�� ����� �����
           MOVE 0 TO SW-LOOP.
           MOVE 1 TO I-ANAF.
           MOVE 1 TO I.
           PERFORM 610 THRU 610-CONT UNTIL SW-END-LOOP.
           GO TO 620.
       610.
           IF I-ANAF = 51 OR PR1(I) = 0
      *--- ��×��� ���� ������ ������ �� �÷� ���÷� �� - 0=������ ���� ��
              IF I = 1 AND PR1(I) = 0
                 INITIALIZE ES_TV_ANAFIM
                 MOVE 0 TO I-ANAF
              END-IF
              SET SW-END-LOOP TO TRUE
              GO TO 610-CONT.

           MOVE PR1(I)(4:3) TO ES_ANAF(I-ANAF).
      ****       ������ ���� ������ �� , 129 ��×�× ��÷�� ���� ����× ��
           ADD 1 TO I
           PERFORM BDIKA-ANAF-TVL129.
           IF KAYAM-ANAF-YES
              ADD 1 TO I-ANAF
           ELSE
              MOVE 0 TO ES_ANAF(I-ANAF)
           END-IF.
       610-CONT.
       620.

      *---                                           ������ ����� �"��
           COMPUTE ES_SUM_ANAF = I-ANAF - 1.

           IF ES_SUM_ANAF = 0
              ADD  1                          TO I-ERR
              MOVE 0003                       TO PO2_ESERKD(I-ERR)
              MOVE '!������ ������ ����� ���' TO PO2_ESERTX(I-ERR)
              MOVE 1                          TO PO2_ESERSG(I-ERR)
              MOVE 1                          TO PO2_ESKERR
              ADD  1                          TO PO2_ESKERR_NR
              GO TO 600-EXIT.

       630.
      *---                                 ����� ��×�× ����� ��� �����
           MOVE 1 TO I.
           PERFORM 650-PRATEI-ANFIM UNTIL I > ES_SUM_ANAF
                                          OR PO2_ESKERR = 1.
       600-EXIT.
           EXIT.
      *-----------------------------------------------------------------
       620-ANAFIM-SHIDRUG SECTION.
      *-----------------------------------------------------------------
       620.
           INITIALIZE EZ-KEY-155-129.
           MOVE 155               TO CT10-TVL-NO.
           MOVE PI1_ESCOMP        TO CT10-HEVRA.
           MOVE 0                 TO CT10-MAHOZ.
           MOVE 0                 TO CT10-ANAF.
           MOVE 0                 TO CT10-MATBEA.
           MOVE 129               TO EZ-NUM-TAT.
           MOVE 0                 TO EZ-129-ANAF.
           MOVE ZERO              TO EZ-ZERO.
           MOVE EZ-KEY-155-129    TO CT10-KEY-KLLI.
           MOVE 0                 TO CT10-NO-SDRI.
           MOVE 0                 TO CT10-IND-OVERIDE.
           MOVE 99999999          TO CT10-DATE.
           SET CT10-OVERIDE-NO    TO TRUE.
           SET CT10-REC-ALL       TO TRUE.
           MOVE 25                TO CT10-NO-REC-SHLIFA.
           CALL 'ELTV010B' USING CT10-AREA-KLALI
                                 RTN-TVL-DATA.
           IF NOT CT10-HZR-TAKIN
              ADD  1  TO I-ERR
              MOVE 55                          TO PO2_ESERKD(I-ERR)
              MOVE 1                           TO PO2_ESERSG(I-ERR)
              MOVE '!  155/129 ��×�× ����� ����� ��'
                   TO PO2_ESERTX(I-ERR)
              MOVE 1                           TO PO2_ESKERR
              ADD  1                           TO PO2_ESKERR_NR
              GO TO 620-EXIT
           END-IF

           MOVE 0 TO I-ANAF.
*********  INITIALIZE ES_TV_ANAFIM.
           PERFORM 621 THRU 622-CONT VARYING I FROM 1 BY 1
   *********                         UNTIL I > 25.
                                     UNTIL I > CT10-TVL-IND.
           GO TO 620-EXIT.
       621.
      *---                                            ������ ������ ���
           IF TUSEQ OF RTN-TVL-DATA(I) = 0
              GO TO 622-CONT
           END-IF.

           IF TUKEY OF RTN-TVL-REC(I)(1:11) NUMERIC
              ADD 1 TO I-ANAF
              MOVE TUKEY OF RTN-TVL-REC(I)(9:3) TO
                   ES_ANAF(I-ANAF)
           END-IF.
       622-CONT.
       620-EXIT.
           EXIT.
      *-----------------------------------------------------------------
       621-SEARCH-ANAF    SECTION.
      *-----------------------------------------------------------------
       621.
           SET I-VEC TO 1.
           SEARCH PR-VEC-ANAF
                  AT END
                     SET SW-ANAF-FOUND-NO    TO TRUE
                     GO TO 621-EXIT
                  WHEN PR-ANAF-NO(I-VEC) =
                       PR1(I)
                       SET SW-ANAF-FOUND-YES TO TRUE
                       GO TO 621-EXIT
           END-SEARCH.
       621-EXIT.
           EXIT.
      *-----------------------------------------------------------------
       650-PRATEI-ANFIM   SECTION.
      *-----------------------------------------------------------------
       650.
      *---                                  ����� ��×�× ����� ��� �����
           MOVE ES_ANAF(I) TO EZ-ANAF.
           MOVE DAMHOV     TO EZ-MHOZ-OVR.
           PERFORM 506-KRIA-TVL006
           IF SW-ANAF-FOUND-06-NO
              GO TO 656
           END-IF.
      **--                70 ��×�� ������ ×��� ����� ������
      *    IF T06SCN = 3
              PERFORM 700-ARSHAOT-AMALOT
      *    END-IF
      **--               USERS -� ������ ����� ����� ������
      *--                                     !!!�×�× �����
      *-   IF T06SCN = 1 AND DASUGU = '1'
      *-      IF EZ-CHGA ='�'
      *-         MOVE '0' TO ES_AMLA_ARSHAA_UPDATE_A(I)
      *-                     ES_AMLA_ARSHAA_UPDATE_B(I)
      *-      ELSE
      *-         MOVE '1' TO ES_AMLA_ARSHAA_UPDATE_A(I)
      *-                     ES_AMLA_ARSHAA_UPDATE_B(I)
      *-      END-IF
      *-      IF EZ-AMLA ='�'
      *-         MOVE '0' TO ES_AMLA_ARSHAA_READ_A(I)
      *-                     ES_AMLA_ARSHAA_READ_B(I)
      *-      ELSE
      *-         MOVE '1' TO ES_AMLA_ARSHAA_READ_A(I)
      *-                     ES_AMLA_ARSHAA_READ_B(I)
      *-      END-IF
      *-   END-IF
      *---                                            USERS:������ ����
           IF EZ-QWRY ='�'
              MOVE '0' TO ES_QWRY(I)
           ELSE
              MOVE '1' TO ES_QWRY(I)
ZMANI         IF ES_ANAF(I) = 180 AND EZ-180-NO
ZMANI         MOVE '0' TO ES_QWRY(I)
ZMANI         END-IF
           END-IF
           IF EZ-CHGA ='�'
              MOVE '0' TO ES_CHGA(I)
           ELSE
              MOVE '1' TO ES_CHGA(I)
           END-IF
           IF EZ-IMMD ='�'
              MOVE '0' TO ES_IMMD(I)
           ELSE
              MOVE '1' TO ES_IMMD(I)
           END-IF
           IF EZ-CHSV ='�'
              MOVE '0' TO ES_CHSV(I)
           ELSE
              MOVE '1' TO ES_CHSV(I)
ZMANI         IF ES_ANAF(I) = 180 AND EZ-180-NO
ZMANI         MOVE '0' TO ES_CHSV(I)
ZMANI         END-IF
           END-IF
           IF EZ-ISHR ='�'
              MOVE '0' TO ES_ISHR(I)
           ELSE
              MOVE '1' TO ES_ISHR(I)
           END-IF
           IF EZ-PRMM = '�'
              MOVE '0' TO ES_MASAX_PREMIUMIM(I)
           ELSE
              MOVE '1' TO ES_MASAX_PREMIUMIM(I)
           END-IF
           IF EZ-PRSH = '�'
              MOVE '0' TO ES_MASAX_SHEABUD(I)
           ELSE
              MOVE '1' TO ES_MASAX_SHEABUD(I)
           END-IF
      **--                    ������ ���× ����� ������ �����
           MOVE '1' TO ES_AMLA_PREMIUM_UPDATE_B(I).
           MOVE '1' TO ES_MISHNE_UPDATE_B(I).
           MOVE '1' TO ES_MESHUTAF_UPDATE_B(I).
           IF DASUGU = '1'
              OR ES_MHOZ = 73
              MOVE '0' TO ES_AMLA_PREMIUM_UPDATE_B(I)
              MOVE '0' TO ES_MISHNE_UPDATE_B(I)
              MOVE '0' TO ES_MESHUTAF_UPDATE_B(I)
           END-IF.
      **--                                  ��×��� ���� ����×
           IF DASUGU = '1'
              IF SW-SOCEN-RONBIT-YES
                 MOVE '1' TO ES_MESHUTAF_UPDATE_B(I)
              END-IF
           END-IF.

      *---   (USERS �� �����-������ ����� ���� ��) USERSANF �×��� �����
           MOVE ES_MHOZ    TO USSHEV OF USERSANF-REC.
           MOVE PI1_ESUSER TO USSNUM OF USERSANF-REC.
           MOVE ES_ANAF(I) TO USANF  OF USERSANF-REC.
???        MOVE 0          TO USDAT  OF USERSANF-REC.
           MOVE EZ-UDATE   TO USDAT  OF USERSANF-REC.
      *---         (�����× �� READ-� ���)��� ���� �� ��� �� START-�����
           START USERSANF
                 KEY NOT LESS EXTERNALLY-DESCRIBED-KEY
                 INVALID KEY
                         ADD 1         TO I-ERR
                         MOVE 0003     TO PO2_ESERKD(I-ERR)
                         MOVE 1        TO PO2_ESERSG(I-ERR)
                         MOVE 1        TO PO2_ESKERR
                         ADD 1         TO PO2_ESKERR_NR
                         GO TO 655.

           READ USERSANF NEXT
                AT END
                GO TO 655.
           IF USSHEV OF USERSANF-REC NOT = ES_MHOZ    OR
              USSNUM OF USERSANF-REC NOT = PI1_ESUSER OR
              USANF  OF USERSANF-REC NOT = ES_ANAF(I)
              GO TO 655.

           IF USIMMD OF USERSANF-REC = '�'
              MOVE '0' TO ES_IMMD(I)
           ELSE
              MOVE '1' TO ES_IMMD(I)
           END-IF
  *******      ����� �� , ���� ���� ��� ÷����×. ×���� ���� ��� ��� ����
  *******                        .�����×� ���� ���� ������ ����� ������
  *******  IF USCHSV OF USERSANF-REC = '�'
  *******     MOVE '0' TO ES_CHSV(I)
  *******  ELSE
  *******     MOVE '1' TO ES_CHSV(I)
  *******  END-IF
           IF USISHR OF USERSANF-REC = '�'
              MOVE '0' TO ES_ISHR(I)
           ELSE
              MOVE '1' TO ES_ISHR(I)
           END-IF
           IF USPRMM OF USERSANF-REC = '�'
              MOVE '0' TO ES_MASAX_PREMIUMIM(I)
           ELSE
              MOVE '1' TO ES_MASAX_PREMIUMIM(I)
           END-IF
      ***                              ����� ���� ��×��
            MOVE '1' TO ES_MASAX_SHEABUD(I).
      **--            USERSANF -� ������ ����� ����� ������
      *-   IF T06SCN = 1
      *-      IF USCHGA OF USERSANF-REC = '�'
      *-         MOVE '0' TO ES_AMLA_ARSHAA_UPDATE_A(I)
      *-                     ES_AMLA_ARSHAA_UPDATE_B(I)
      *-      ELSE
      *-         MOVE '1' TO ES_AMLA_ARSHAA_UPDATE_A(I)
      *-                     ES_AMLA_ARSHAA_UPDATE_B(I)
      *-      END-IF
      *-      IF USAMLA OF USERSANF-REC = '�'
      *-         MOVE '0' TO ES_AMLA_ARSHAA_READ_A(I)
      *-                     ES_AMLA_ARSHAA_READ_B(I)
      *-      ELSE
      *-         MOVE '1' TO ES_AMLA_ARSHAA_READ_A(I)
      *-                     ES_AMLA_ARSHAA_READ_B(I)
      *-      END-IF
      *-   END-IF

      **--                    ������ ���× ����� ������ �����
           MOVE '1' TO ES_AMLA_PREMIUM_UPDATE_B(I).
           MOVE '1' TO ES_MISHNE_UPDATE_B(I).
           MOVE '1' TO ES_MESHUTAF_UPDATE_B(I).
           IF DASUGU = '1'
              OR ES_MHOZ = 73
              MOVE '0' TO ES_AMLA_PREMIUM_UPDATE_B(I)
              MOVE '0' TO ES_MISHNE_UPDATE_B(I)
              MOVE '0' TO ES_MESHUTAF_UPDATE_B(I)
           END-IF.
      **--                                  ��×��� ���� ����×
           IF DASUGU = '1'
              IF SW-SOCEN-RONBIT-YES
                 MOVE '1' TO ES_MESHUTAF_UPDATE_B(I)
              END-IF
           END-IF.
 *******************************************************************************
       655.
           PERFORM 700-ARSHAOT-IDKUN-POLISA.
       656.
           ADD 1 TO I.
       650-EXIT.
           EXIT.
      *-----------------------------------------------------------------
       700-ARSHAOT-AMALOT  SECTION.
      *-----------------------------------------------------------------
       700.

      *---                                  ������� ������ ������ ����×
      *---                   ��� ������ ��� ����� ���� - ���� ������ ��
      *---                                      ����=2  ���=1:����� ÷��
           IF DASUGU = '0'
              MOVE '1' TO ES_AMLA_ARSHAA_READ_A(I)
              MOVE '1' TO ES_AMLA_ARSHAA_READ_B(I)
              MOVE '1' TO ES_AMLA_ARSHAA_UPDATE_A(I)
              MOVE '1' TO ES_AMLA_ARSHAA_UPDATE_B(I)
              MOVE '1' TO ES_MISHNE_UPDATE_B(I)
              MOVE '1' TO ES_MASAX_PREMIUMIM(I)
              MOVE '1' TO ES_MASAX_SHEABUD(I)
              GO TO 700-EXIT.
      *---                                           ����� ����� ������
           IF T70AAM = ' '
              MOVE '1' TO ES_AMLA_ARSHAA_READ_A(I)
              MOVE '1' TO ES_AMLA_ARSHAA_READ_B(I)
              GO TO 201.
           IF T70AAM = '0'
              MOVE '0' TO ES_AMLA_ARSHAA_READ_A(I)
              MOVE '0' TO ES_AMLA_ARSHAA_READ_B(I)
              GO TO 201.
           IF T70AAM = '1'
              MOVE '1' TO ES_AMLA_ARSHAA_READ_A(I)
              MOVE '0' TO ES_AMLA_ARSHAA_READ_B(I)
              GO TO 201.
           IF T70AAM = '2'
              MOVE '0' TO ES_AMLA_ARSHAA_READ_A(I)
              MOVE '1' TO ES_AMLA_ARSHAA_READ_B(I)
              GO TO 201.
           IF T70AAM = '3'
              MOVE '1' TO ES_AMLA_ARSHAA_READ_A(I)
              MOVE '1' TO ES_AMLA_ARSHAA_READ_B(I)
              GO TO 201.
           IF T70AAM = '4'
              MOVE '0' TO ES_AMLA_ARSHAA_READ_A(I)
              MOVE '0' TO ES_AMLA_ARSHAA_READ_B(I)
              GO TO 201.
       201.

      *---                                           ����� ����� ������
zmani      IF T70IAM = ' '
zmani         MOVE '1' TO ES_AMLA_ARSHAA_UPDATE_A(I)
zmani         MOVE '1' TO ES_AMLA_ARSHAA_UPDATE_B(I)
zmani         GO TO 202.
           IF T70IAM = '0'
              MOVE '0' TO ES_AMLA_ARSHAA_UPDATE_A(I)
              MOVE '0' TO ES_AMLA_ARSHAA_UPDATE_B(I)
              GO TO 202.
           IF T70IAM = '1'
              MOVE '1' TO ES_AMLA_ARSHAA_UPDATE_A(I)
              MOVE '0' TO ES_AMLA_ARSHAA_UPDATE_B(I)
              GO TO 202.
           IF T70IAM = '2'
              MOVE '0' TO ES_AMLA_ARSHAA_UPDATE_A(I)
              MOVE '1' TO ES_AMLA_ARSHAA_UPDATE_B(I)
              GO TO 202.
           IF T70IAM = '3'
              MOVE '1' TO ES_AMLA_ARSHAA_UPDATE_A(I)
              MOVE '1' TO ES_AMLA_ARSHAA_UPDATE_B(I)
              GO TO 202.
           IF T70IAM = '4'
              MOVE '0' TO ES_AMLA_ARSHAA_UPDATE_A(I)
              MOVE '0' TO ES_AMLA_ARSHAA_UPDATE_B(I)
              GO TO 202.
       202.
       700-EXIT.
           EXIT.
      *-----------------------------------------------------------------
       700-ARSHAOT-IDKUN-POLISA      SECTION.
      *-----------------------------------------------------------------
       700.
           IF DASUGU = '0'
              MOVE '1' TO ES_CHGA(I)
              GO TO 700-EXIT
           END-IF.

           SET I-CHGA TO 1.
           SEARCH TV-CHGA-O
                  AT END
                     GO TO 700-EXIT
                  WHEN TV-ANAF70(I-CHGA)     = ES_ANAF(I)
                       AND TV-ARSHAA(I-CHGA) = 9
                       MOVE '0' TO ES_CHGA(I)
                       GO TO 700-EXIT
           END-SEARCH.
       700-EXIT.
           EXIT.
      *-----------------------------------------------------------------
       551-READ-TVL70    SECTION.
      *-----------------------------------------------------------------
      **--
       551.
           MOVE 70                TO CT10-TVL-NO.
           MOVE PI1_ESCOMP        TO CT10-HEVRA.
           MOVE ES_MHOZ OF PO4    TO CT10-MAHOZ.
           MOVE ZERO              TO CT10-ANAF.
           MOVE ZERO              TO CT10-MATBEA.
           MOVE SPACES            TO EZ-X20-T.
           MOVE PI1_ESUSER        TO EZ-X10-T.
           MOVE EZ-X20-T          TO CT10-KEY-KLLI.
           MOVE ZERO              TO CT10-DATE.
           MOVE ZERO              TO CT10-NO-SDRI.
           SET CT10-HEMSH-NO      TO TRUE.
           SET CT10-OVERIDE-NO    TO TRUE.
           MOVE ZERO              TO CT10-IND-OVERIDE.
           SET CT10-REC-ALL       TO TRUE.
           MOVE ZERO              TO CT10-NO-REC-SHLIFA.
           CALL 'ELTV010B'  USING CT10-AREA-KLALI RTN-TVL-DATA.
           IF NOT CT10-HZR-TAKIN
              ADD  1                         TO I-ERR
              MOVE 0200                      TO PO2_ESERKD(I-ERR)
              MOVE 1                         TO PO2_ESERSG(I-ERR)
              MOVE '!��� ����� ���� �������' TO PO2_ESERTX(I-ERR)
              MOVE 1                         TO PO2_ESKERR
              ADD  1                         TO PO2_ESKERR_NR
              GO TO 551-EXIT.
           MOVE TUREC OF RTN-TVL-DATA(1) TO TVL70-REC.
           INITIALIZE TVL-CHGA.
           MOVE T70ANF1                  TO TV-CHGA-O(1).
           MOVE T70ANF2                  TO TV-CHGA-O(2).
           MOVE T70ANF3                  TO TV-CHGA-O(3).
           MOVE T70ANF4                  TO TV-CHGA-O(4).
           MOVE T70ANF5                  TO TV-CHGA-O(5).
           MOVE T70ANF6                  TO TV-CHGA-O(6).
           MOVE T70ANF7                  TO TV-CHGA-O(7).
           MOVE T70ANF8                  TO TV-CHGA-O(8).
           MOVE T70ANF9                  TO TV-CHGA-O(9).
           MOVE T70ANF10                 TO TV-CHGA-O(10).
       551-EXIT.
           EXIT.
      *-----------------------------------------------------------------
       552-SOCEN-RONBIT  SECTION.
      *-----------------------------------------------------------------
      **--                                                 155/106 ��×�
       552.
           SET SW-SOCEN-RONBIT-NO TO TRUE.

           MOVE 155               TO CT10-TVL-NO.
           MOVE PI1_ESCOMP        TO CT10-HEVRA.
           MOVE ES_MHOZ OF PO4    TO CT10-MAHOZ.
           MOVE ZERO              TO CT10-ANAF.
           MOVE ZERO              TO CT10-MATBEA.
           MOVE SPACE             TO EZ-CT10-106.
           MOVE 106               TO EZ-KEY-TVL.
           MOVE T70SCN            TO EZ-KEY-SOCEN.
           MOVE ZERO              TO EZ-KEY-FIL1.
           MOVE EZ-CT10-106       TO CT10-KEY-KLLI.
           MOVE 0                 TO CT10-NO-SDRI
                                     CT10-IND-OVERIDE.
           MOVE 99999999          TO CT10-DATE.
           SET CT10-OVERIDE-NO    TO TRUE.
           SET CT10-REC-SINGL     TO TRUE.
           CALL 'ELTV010B'  USING CT10-AREA-KLALI
                                  RTN-TVL-DATA.
           IF CT10-HZR-TAKIN
              MOVE TUREC(1) TO TVL106-REC
              IF T155TK OF TVL106-REC = '�'
                 SET SW-SOCEN-RONBIT-YES TO TRUE
              END-IF
           END-IF.
       552-EXIT.
           EXIT.
      *-----------------------------------------------------------------
       800-PRATIM-NOSAFIM      SECTION.
      *-----------------------------------------------------------------
       800.
      *---                                             ...���� ���� ���
********** IF PI1_ESACC1 NOT= 0
              IF DASUGS = ' '
                 MOVE 0 TO ES_SOC_MADANES
              END-IF
   **********                                                    ����
              IF DASUGS = '�'
                 MOVE 1 TO ES_SOC_MADANES
              END-IF
              IF DASUGS = '×'
                 MOVE 2 TO ES_SOC_MADANES
                 PERFORM 810-MADANES-SOC-B
              END-IF
   **********                                                     ���
              IF DAKDSC = '2'
                 MOVE 3 TO ES_SOC_MADANES
              END-IF
              IF DAKDSC = '3'
                 MOVE 4 TO ES_SOC_MADANES
              END-IF
   **********                                                  ��×���
              IF DASUGU = '1' AND SW-SOCEN-RONBIT-YES
                 MOVE 5 TO ES_SOC_MADANES
              END-IF
ELAL  ********                      �� ��÷�� ����� ���� ������ ���×�
              PERFORM 830-AL.
********** END-IF.
           IF ischek   = '�'
             move 2 to ES_ISOH_BDIKA
             go to 800-exit.
           move FILLER-DDS OF USERSS1-REC to ez-iso1.
           IF ez-iso1(16:1)  not = ' ' and not = '0'  and not = '�'
              perform 820-iso.
**************                 �×��× ×���� ���� ����� ���� ��� ���×�
              PERFORM 851-BDK-SHINUY-HISHUV-HOVA.
       800-EXIT.
           EXIT.
      *-----------------------------------------------------------------
       810-MADANES-SOC-B       SECTION.
      *-----------------------------------------------------------------
       810.
      *--- �����× ��� '� ����� �� ������-���� �� '× ���� ��� ����� ����
      *---                        ������ ���� ���� ������� - ���� �� ��

           IF ES_SOCHEN(1) = 999999 OR T70SCB = 0
              GO TO 810-EXIT
           END-IF.
           INITIALIZE EZ-SUM-SOCHEN
                      TV-SOCHNIM.
           MOVE ES_TV_SOCHNIM TO TV-SOCHNIM.
           SET I-TV-SOC TO 1.
      *---                      ������� ������ '� ���� �����:���� �� ��
           SEARCH TV-SOCHNIM-O
              AT END
                 ADD 1 TO ES_SUM_SOCHEN
                 MOVE ES_SUM_SOCHEN TO I-SOC
                 MOVE T70SCB TO ES_SOCHEN(I-SOC)
                 PERFORM 540-PRATIM-SOCHEN
           END-SEARCH.

       810-EXIT.
           EXIT.
      *-----------------------------------------------------------------
       899-MADANES-SOC-B       SECTION.
      *-----------------------------------------------------------------
       810.
      *---                                              !!!!!!!!!!!���×
      *--- �����× ��� '� ����� �� ������-���� �� '× ���� ��� ����� ����
      *---                            ������ ���× ���� ������ - ���� ��
      *---                        ������ ���� ���� ������� - ���� �� ��

           IF ES_SOCHEN(1) = 999999 OR T70SCB = 0
              GO TO 810-EXIT
           END-IF.
           INITIALIZE EZ-SUM-SOCHEN
                      TV-SOCHNIM.
           MOVE ES_TV_SOCHNIM TO TV-SOCHNIM.
           INITIALIZE ES_TV_SOCHNIM.
           MOVE 1 TO I.
           SET I-TV-SOC TO 1.
           SEARCH TV-SOCHNIM-O
      *---                             ������� ����� ���× '� ���� �����
              AT END
                 ADD 1 TO ES_SUM_SOCHEN
                 MOVE T70SCB TO ES_SOCHEN(I)
                 MOVE 1 TO I-SOC
                 PERFORM 540-PRATIM-SOCHEN
      *---                                    ������ ���× '� ���� �����
              WHEN TV-SOCHEN(I-TV-SOC) = T70SCB
                 MOVE TV-SOCHEN      (I-TV-SOC) TO ES_SOCHEN(I)
                 MOVE TV-SOCHEN-NAME (I-TV-SOC) TO ES_SOCHEN_NAME(I)
                 MOVE TV-SOCHEN-TZ   (I-TV-SOC) TO ES_SOCHEN_TZ(I)
                 MOVE TV-SOCHEN-ADDRS(I-TV-SOC) TO ES_SOCHEN_ADDRS(I)
                 MOVE TV-SOCHEN-TEL  (I-TV-SOC) TO ES_SOCHEN_TEL(I)
                 MOVE TV-SOCHEN-MAIL (I-TV-SOC) TO ES_SOCHEN_MAIL(I)
           END-SEARCH.

      *---                         ����� ���� ������ ������� ��×� �����
           MOVE 0 TO J.
           PERFORM ES_SUM_SOCHEN TIMES
              ADD 1 TO J
              IF TV-SOCHEN(J) NOT= T70SCB
                 ADD 1 TO I
                 MOVE TV-SOCHEN      (J) TO ES_SOCHEN(I)
                 MOVE TV-SOCHEN-NAME (J) TO ES_SOCHEN_NAME(I)
                 MOVE TV-SOCHEN-TZ   (J) TO ES_SOCHEN_TZ(I)
                 MOVE TV-SOCHEN-ADDRS(J) TO ES_SOCHEN_ADDRS(I)
                 MOVE TV-SOCHEN-TEL  (J) TO ES_SOCHEN_TEL(I)
                 MOVE TV-SOCHEN-MAIL (J) TO ES_SOCHEN_MAIL(I)
              END-IF
           END-PERFORM.

           INITIALIZE LN020-PARM.
           MOVE EZ-MHOZ-NUM  TO LN020-MAHOZ.
           MOVE ES_SOCHEN(1) TO LN020-SOXEN.
           MOVE 99999999     TO LN020-DATE.
           CALL 'ELNT020R' USING LN020-PARM.
           MOVE LN020-USER   TO ES_USER_HATAM.
           MOVE LN020-USNM   TO ES_NAME_HATAM.

       810-EXIT.
           EXIT.
      *-----------------------------------------------------------------
       820-iso                 SECTION.
      *-----------------------------------------------------------------
       820.
           move 1 to ES_ISOH_BDIKA.
           move 0 to prm_to_date.
           move ez-iso1(16:1) to PRM_ERRMSG(80:1).
   ******  if ez-iso1(16:1) = '1'
              CALL 'ELBDSAPS' using        PI1_ESUSER
                                           SW_SAPSOC.
g*         if PI1_ESUSER = 'TSTHATAM  ' or
g*            PI1_ESUSER = 'GALINAR   ' or
g*            PI1_ESUSER = 'SOCMAZAL  ' or
g*            SW-SAP     = '1'          or
g*            SW_SAPSOC  = '1'
g*            move 1 to ES_ISOH_BDIKA
g*            move 0 to     ES_ISOH_SAP
g*            move 0 to     iso-ISHUR-USER
g*            go to 850.
           call 'EIHARSB' using            PI1_ESUSER
                                           PRM_MAHOZ
                                           PRM_KPEUL
                                           PRM_USERID
                                           PRM_SOCNID
                                           PRM_DTMSM
                                           PRM_DTKMSM
                                           PRM_TAFKID
                                           PRM_KODHZR
                                           PRM_ERRMSG
                                           PRM_to_date.
           move PRM_kodhzr      to ES_ISOH_SAP.
           move PI1_ESUSER      to iso-ISHUR-USER.
g*     850.
           move 17              to ISO-SUG-HESKEM.
           move 999999          to iso-SOCHEN-A  iso-SOCHEN-b.
           CALL 'ELARSHAR' USING  iso-ISHUR-PARAM.
           move iso-ISHUR-KOD   to ES_ISOH_ISHUR_MEUHAD.

           MOVE PI1_ESUSER      TO ISO-ISHUR-USER.
           MOVE 24              TO ISO-SUG-HESKEM.
           MOVE 999999          TO ISO-SOCHEN-A  ISO-SOCHEN-B.
           CALL 'ELARSHAR' USING  ISO-ISHUR-PARAM.
           MOVE ISO-ISHUR-KOD   TO ES_ISOH_PATUR.
hzh bg**      ����� ����� ����� ,1 =ES_HAZHARA ���� ����� ����� ����×
           MOVE PI1_ESUSER      TO HZH-USER.
           MOVE EZ-UDATE        TO HZH-DATE.
           MOVE ' '             TO HZH-KOD.
           CALL 'ELBDHZHR' USING  HZH-PARAM.
           MOVE HZH-KOD         TO ES_HAZHARA.
hzh en**                                       ���� ����� ����� ����×
       820-EXIT.
           EXIT.
      *-----------------------------------------------------------------
       900-HARSHAOT-IMAGE      SECTION.
      *-----------------------------------------------------------------
       900.
    *****       ������ ��×� ��� ����� IMAGE-× ������� ���� ���� ���
           IF ES_SUG_USER = 0
              MOVE 1 TO ES_IMAGE
              GO TO 900-EXIT
           END-IF.
           MOVE 0 TO ES_IMAGE.
           MOVE PI1_ESUSER    TO ARS-ISHUR-USER.
           MOVE 26            TO ARS-SUG-HESKEM.
           MOVE 999999        TO ARS-SOCHEN-A.
           MOVE 999999        TO ARS-SOCHEN-B.
           CALL 'ELARSHAR' USING ARS-ISHUR-PARAM
           IF ARS-ISHUR-KOD = '1'
              MOVE 1 TO ES_IMAGE
           END-IF.
       900-EXIT.
           EXIT.
      *-----------------------------------------------------------------
       540-PRATIM-SOCHEN           SECTION.
      *-----------------------------------------------------------------
       540.
      *---                                              ���� ���� �����
           SET  CT50-MDSAV           TO TRUE.
           SET  CT50-KRIA-DIRECT     TO TRUE.
           SET  CT50-HZR-TAKIN       TO TRUE.
           MOVE ES_SOCHEN(I-SOC)     TO M2HESH OF RTN-ELMAV-REC.
           MOVE 1                    TO M2MTBA OF RTN-ELMAV-REC.
           MOVE ES_MHOZ              TO M2HVRA OF RTN-ELMAV-REC.
           CALL 'ELTV050B'  USING  CT50-AREA-KLALI RTN-ELMAV-REC.
           IF NOT CT50-HZR-TAKIN
              THEN
                   MOVE ALL '*' TO M2LAK OF RTN-ELMAV-REC.

           MOVE M2LAK  OF RTN-ELMAV-REC TO ES_SOCHEN_NAME(I-SOC).
      *---                               !����� ��� - ��� ���× �� �� ��
      *---            SPACE ���� �����× ���×� ��� 31 ���÷× ������ �×���
           INITIALIZE EZ-SOC-NAME-31.
           MOVE ES_SOCHEN_NAME(I-SOC) TO EZ-SOC-NAME-31(2:30).
           MOVE 0 TO I-TAV.
           MOVE 0 TO SW-TAVIM.
           PERFORM VARYING I-TAV FROM 1 BY 1 UNTIL I-TAV > 31 OR
                                                   SW-TAVIM-END
              IF EZ-SOC-NAME-31(I-TAV:1) = "'" OR '"' OR '.' OR '#'
                 OR ')' OR '('
      *---                      31 ��� 30 ���÷× ������ ���� �� - 1 ����
                 SUBTRACT 1 FROM I-TAV
                 MOVE ' ' TO ES_SOCHEN_NAME(I-SOC)(I-TAV:1)
                 SET SW-TAVIM-END TO TRUE
              END-IF
      *---                        ����� ��� ��-���×� ����� ������ �����
              IF EZ-SOC-NAME-31(I-TAV:1) NOT= ' '
                 SET SW-TAVIM-END TO TRUE
              END-IF
           END-PERFORM.

      *---                                                          �"�
           MOVE M2TZNK OF RTN-ELMAV-REC TO ES_SOCHEN_TZ(I-SOC).
      *---                                                        �×���
           MOVE M2ADDR OF RTN-ELMAV-REC TO ES_SOCHEN_ADDRS(I-SOC).
      *---                                                        �����
           MOVE M2TELP OF RTN-ELMAV-REC TO ES_SOCHEN_TEL(I-SOC).
      *---                                                         ����
           PERFORM 541-SOCHEN-MAIL.
      ***  MOVE M2**** TO ES_SOCHEN_MAIL(I-SOC).

      *---                                      ���� ��×� '× �� '� ����
           IF DASUGS NOT= ' '
              PERFORM 542-SOCHEN-AB
           END-IF.

       540-EXIT.
           EXIT.
      *-----------------------------------------------------------------
       541-SOCHEN-MAIL        SECTION.
      *-----------------------------------------------------------------
       541.
      *---                                           ���� �� ���� �×���
           MOVE ES_MHOZ          TO MSMHOZ OF ESMAVL-REC.
           MOVE ES_SOCHEN(I-SOC) TO MSACC1 OF ESMAVL-REC.
           MOVE 1                TO MSMTBA OF ESMAVL-REC.
           READ ESMAVL
              INVALID KEY
                 GO TO 541-EXIT.
           MOVE MSMAIL1 OF ESMAVL-REC(1:40) TO ES_SOCHEN_MAIL(I-SOC).

       541-EXIT.
           EXIT.
      *-----------------------------------------------------------------
       542-SOCHEN-AB   SECTION.
      *-----------------------------------------------------------------
       542.
      *---          �� �� '� ���� ��� - ���� ��×� ����×� 122 ��×�� ����
           MOVE 122                    TO CT10-TVL-NO.
           MOVE PI1_ESCOMP             TO CT10-HEVRA.
           MOVE 0                      TO CT10-MAHOZ.
           MOVE 0                      TO CT10-ANAF.
           MOVE 0                      TO CT10-MATBEA.
           MOVE SPACES                 TO EZ-CT10-KEY-122
           MOVE ES_SOCHEN(I-SOC)       TO EZ-CT10-T122-SOC
           MOVE EZ-CT10-KEY-122        TO CT10-KEY-KLLI.
           MOVE ZERO                   TO CT10-NO-SDRI.
           SET  CT10-OVERIDE-NO        TO TRUE.
           MOVE 99999999               TO CT10-DATE.
           SET CT10-REC-SINGL        TO TRUE.
           CALL 'ELTV010B'  USING CT10-AREA-KLALI RTN-TVL-DATA.
           IF CT10-HZR-TAKIN
              MOVE '1' TO ES_SOCHEN_AB(I-SOC)
           ELSE
              MOVE '2' TO ES_SOCHEN_AB(I-SOC)
           END-IF.

       542-NEXT.
      **--                        (��×���) - �"��� ����� ���� ��� ���×�
      **-- ����� �� ��×� ����� ������× ��� 20 -� 25 ����� �� �×�� ÷����
      **--            (MAX 10) ������ ��×� ���� ��� ����� ��� �×�× ����
           MOVE 268                    TO CT10-TVL-NO.
           MOVE PI1_ESCOMP             TO CT10-HEVRA.
           MOVE ES_MHOZ                TO CT10-MAHOZ.
           MOVE 25                     TO CT10-ANAF.
           MOVE 1                      TO CT10-MATBEA.
           MOVE SPACE                  TO EZ-CT10-268-REC .
           MOVE ES_SOCHEN(I-SOC)       TO EZ-CT10-T268-SOC
           MOVE EZ-CT10-KEY-268        TO CT10-KEY-KLLI.
           MOVE ZERO                   TO CT10-NO-SDRI
                                          CT10-IND-OVERIDE.
           MOVE 99999999               TO CT10-DATE.
           SET CT10-REC-SINGL          TO TRUE.
           SET CT10-OVERIDE-NO         TO TRUE.
      **--           999 �� ���� 25 �� ��� �� �� , ����� 2 ��×� �����
           PERFORM 2 TIMES
             CALL 'ELTV010B'  USING CT10-AREA-KLALI RTN-TVL-DATA
             END-CALL
             IF CT10-HZR-TAKIN
                MOVE '3' TO ES_SOCHEN_AB(I-SOC)
                GO TO 542-EXIT
             END-IF
             MOVE 999 TO CT10-ANAF
           END-PERFORM.
       542-EXIT.
           EXIT.
      *-----------------------------------------------------------------
       BNIA-LDA        SECTION.
      *-----------------------------------------------------------------
       BL.
      *---                                                     LDA ���×
           IF ES_MHOZ = 0
      ***     MOVE 4 TO ES_MHOZ.
              PERFORM 001-MHOZ-IKARI
              IF PO2_ESKERR = 1
                 GO TO BL-EXIT
              END-IF
           END-IF.

           MOVE 0 TO EZ-LO-TAKIN.
           MOVE PI1_ESFILR(1:8) TO EZ-MAHSHEV.
           CALL 'ES#AUTOLDA' USING PI1_ESSVIV
                                   ES_MHOZ
                                   PI1_ESUSER
                                   ES_SUG_USER
                                   EZ-LO-TAKIN
                                   PI1_ESACC1
                                   EZ-MAHSHEV.

           IF EZ-LO-TAKIN = 2
              ADD  1                 TO I-ERR
              MOVE 0002              TO PO2_ESERKD(I-ERR)
              MOVE 1                 TO PO2_ESERSG(I-ERR)
              MOVE EZ-MAHSHEV        TO PO2_ESFLDN(I-ERR)
              MOVE '!����� �� ���×� �×�×�' TO PO2_ESERTX(I-ERR)
              MOVE 1                 TO PO2_ESKERR
              ADD  1                 TO PO2_ESKERR_NR
              GO TO BL-EXIT.

           IF EZ-LO-TAKIN = 1
              ADD  1                 TO I-ERR
              MOVE 0009              TO PO2_ESERKD(I-ERR)
              MOVE 1                 TO PO2_ESERSG(I-ERR)
              MOVE '!����� LDA ���×' TO PO2_ESERTX(I-ERR)
              MOVE 1                 TO PO2_ESKERR
              ADD  1                 TO PO2_ESKERR_NR
              GO TO BL-EXIT.

           ACCEPT EZ-LDA FROM LDA-AREA1.

       BL-EXIT.
           EXIT.
      *-----------------------------------------------------------------
       BNIA-DTA        SECTION.
      *-----------------------------------------------------------------
       BT.
      *---                                        LDA ��� ELDTAARA ���×
           MOVE '1' TO EZ-MAKOR.
           CALL 'ELBLDPRM' USING  EZ-MAKOR
                                  DATA-AREA1.

      *    MOVE PI1_ESCOMP OF PI1 TO DACOMP.
      *    MOVE PI1_ESUSRK   TO DASUGU.
      *    IF  PI1_ESUSRK = 1
      *        MOVE ZERO         TO DASUGU.
      *    IF  PI1_ESUSRK = 2
      *        MOVE 1            TO DASUGU.
      *---                                                        ÷����
           MOVE '1'                  TO DASHDR.
           MOVE damhoz TO damhoz.

       BT-EXIT.
           EXIT.
      *-----------------------------------------------------------------
       001-MHOZ-IKARI     SECTION.
      *-----------------------------------------------------------------
       001.
      *---    ���� ���� ��� USERPROFILE-�� ������ ����� ���×� ���� ��×�
      *---                                      ������� ����� �×�� �"��
           MOVE PI1_ESCOMP TO NTCOMP.
           INITIALIZE NTMHOZ OF ELNTUSR-REC
                      NTDATE OF ELNTUSR-REC
                      NTUSER OF ELNTUSR-REC.
           START ELNTUSR
                 KEY NOT LESS EXTERNALLY-DESCRIBED-KEY
                 INVALID KEY
                   ADD 1         TO I-ERR
                   MOVE 0001     TO PO2_ESERKD(I-ERR)
                   MOVE 'ESMHZN' TO PO2_ESFLDN(I-ERR)
                   MOVE '!����� elntusr��×�� ����' TO PO2_ESFLDN(I-ERR)
                   MOVE 1        TO PO2_ESERSG(I-ERR)
                   MOVE 1        TO PO2_ESKERR
                   ADD 1         TO PO2_ESKERR_NR
                   GO TO 001-EXIT.


           MOVE 0 TO SW-LOOP.
           MOVE 0 TO I.
           PERFORM 002 THRU 002-CONT UNTIL SW-END-LOOP.
           GO TO 003.
       002.

           READ ELNTUSR NEXT
                AT END
                   SET SW-END-LOOP TO TRUE
                   GO TO 002-CONT.
           IF NTCOMP OF ELNTUSR-REC = PI1_ESCOMP AND
              NTUSER OF ELNTUSR-REC = PI1_ESUSER
              MOVE NTMHOZ OF ELNTUSR-REC TO ES_MHOZ
              SET SW-END-LOOP TO TRUE
              GO TO 002-CONT.

       002-CONT.
       003.


       001-EXIT.
           EXIT.
      *-----------------------------------------------------------------
       830-AL                  SECTION.
      *-----------------------------------------------------------------
       830.
           MOVE 0 TO ES_ZEVET_AL.
           MOVE PI1_ESUSER    TO ARS-ISHUR-USER.
           MOVE 16            TO ARS-SUG-HESKEM.
           MOVE 999999        TO ARS-SOCHEN-A.
           MOVE 999999        TO ARS-SOCHEN-B.
           CALL 'ELARSHAR' USING  ARS-ISHUR-PARAM
           IF ARS-ISHUR-KOD = '1'
           move 1               TO ES_ZEVET_AL.
       830-EXIT.
           EXIT.
      *-----------------------------------------------------------------
       851-BDK-SHINUY-HISHUV-HOVA SECTION.
      *-----------------------------------------------------------------
       000.
           MOVE PI1_ESUSER    TO ARS-ISHUR-USER.
           MOVE 49            TO ARS-SUG-HESKEM.
           MOVE 999999        TO ARS-SOCHEN-A.
           MOVE 999999        TO ARS-SOCHEN-B.
           CALL 'ELARSHAR' USING ARS-ISHUR-PARAM.
           IF ARS-ISHUR-KOD = '1'
              MOVE '1' TO ES_DMAI_POLISA_TOS_INF.
       999.     EXIT.
      *-----------------------------------------------------------------
       901-MHOZ       SECTION.
      *-----------------------------------------------------------------
       901.
      *---                                               OVERIDE-� ����
           MOVE 0                    TO CT20-TVL-NO.
           MOVE PI1_ESCOMP           TO EZ20-COMP
           MOVE PI1_ESMHOZ           TO EZ20-MHOZ.
           MOVE EZ20-KOD-KEY         TO CT20-KOD-KEY.
           CALL 'ELTV020B'   USING CT20-AREA-KLALI TVLKOD-REC.
           MOVE TKREC OF TVLKOD-REC  TO TVL00-REC.
           MOVE T00OVR OF TVL00-REC  TO EZ-MHOZ-OVR.
           MOVE T00OVR OF TVL00-REC  TO DAMHOV.

       901-EXIT.
           EXIT.
      *-----------------------------------------------------------------
       S-SOF           SECTION.
      *-----------------------------------------------------------------
       S00.
           CLOSE USERSANF
                 ELNTUSR
                 USERSS1
                 RSOCENF
                 ELMAVL
                 ESMAVL.

      *---            �×�÷� ���� ����× ����� �×��� INPUT ������� �×���
           PERFORM WRITE-LOG.

       S-EXIT.
           EXIT.
      *-----------------------------------------------------------------
       WRITE-LOG         SECTION.
      *-----------------------------------------------------------------
       WL.
      *---            �×�÷� ���� ����× ����� �×��� INPUT ������� �×���

      *---        ���÷�-97,����� �� ����-98,����-99 :�����× PI1_ESPEUL
           MOVE 2003 TO PI1_ESTASK.
           IF PO2_ESKERR = 0
              IF PO2_ESKERR_NR = 0
                 MOVE 99 TO PI1_ESPEUL
              ELSE
                 MOVE 98 TO PI1_ESPEUL
              END-IF
           ELSE
              MOVE 97 TO PI1_ESPEUL
           END-IF.
           CALL 'ESPRMIB' USING PI1 PI3 PO2.

       WL-EXIT.
           EXIT.
      *-----------------------------------------------------------------
       BDIKA-ANAF-TVL129  SECTION.
      *-----------------------------------------------------------------
       01.
           SET KAYAM-ANAF-NO      TO TRUE.
           MOVE 155               TO CT10-TVL-NO.
           MOVE PI1_ESCOMP        TO CT10-HEVRA.
           MOVE 0                 TO CT10-MAHOZ.
           MOVE 0                 TO CT10-ANAF.
           MOVE 0                 TO CT10-MATBEA.
           MOVE SPACE             TO CT10-KEY-KLLI.
           MOVE 129               TO EZ-NUM-TAT.
           MOVE ES_ANAF(I-ANAF)   TO EZ-129-ANAF.
           MOVE ZERO              TO EZ-ZERO.
           MOVE EZ-KEY-155-129    TO CT10-KEY-KLLI.
           MOVE 0                 TO CT10-NO-SDRI.
           MOVE 0                 TO CT10-IND-OVERIDE.
           MOVE 99999999          TO CT10-DATE.
           SET CT10-OVERIDE-NO    TO TRUE.
           SET CT10-REC-SINGL     TO TRUE.
           CALL 'ELTV010B'  USING CT10-AREA-KLALI
                                     RTN-TVL-DATA.
           IF CT10-HZR-TAKIN
              SET KAYAM-ANAF-YES TO TRUE
           END-IF.
       99-EXIT.
           EXIT.
ZMANI *-----------------------------------------------------------------
ZMANI  155-200-ZMANI      SECTION.
ZMANI *-----------------------------------------------------------------
ZMANI  99Z.
ZMANI      INITIALIZE EZ-KEY-155-200.
ZMANI      MOVE 155               TO CT155-TVL-NO.
ZMANI      MOVE PI1_ESCOMP        TO CT155-HEVRA.
ZMANI      MOVE 0                 TO CT155-MAHOZ.
ZMANI      MOVE 0                 TO CT155-ANAF.
ZMANI      MOVE 0                 TO CT155-MATBEA.
ZMANI      MOVE 200               TO EZ-180-TAT.
ZMANI      MOVE 180               TO EZ-180-ANAF.
ZMANI      MOVE SPACES            TO EZ-180-NR.
ZMANI      MOVE ZERO              TO EZ-180-ZERO.
ZMANI      MOVE EZ-KEY-155-200    TO CT155-KEY-KLLI.
ZMANI      MOVE 0                 TO CT155-NO-SDRI.
ZMANI      MOVE 0                 TO CT155-IND-OVERIDE.
ZMANI      MOVE 99999999          TO CT155-DATE.
ZMANI      SET CT155-OVERIDE-NO   TO TRUE.
ZMANI      SET CT155-REC-ALL      TO TRUE.
ZMANI      MOVE 25                TO CT155-NO-REC-SHLIFA.
ZMANI      CALL 'ELTV155B' USING CT155-AREA-KLALI
ZMANI                            RTN-TVL-DATA.
ZMANI      IF NOT CT155-HZR-TAKIN
ZMANI         GO TO 99Z-EXIT
ZMANI      END-IF
ZMANI
ZMANI      PERFORM 9Z1 THRU 99Z-CONT VARYING I FROM 1 BY 1
ZMANI                                UNTIL I > CT155-TVL-IND.
ZMANI      GO TO 99Z-EXIT.
ZMANI  9Z1.
ZMANI *---                                            ������ ������ ���
ZMANI      IF TUSEQ OF RTN-TVL-DATA(I) = 0
ZMANI         GO TO 99Z-CONT
ZMANI      END-IF.
ZMANI
ZMANI      IF TUREC OF RTN-TVL-REC(I)(1:10) = PI1_ESUSER OR
              TUREC OF RTN-TVL-REC(I)(1:10) = 'ALL       '
ZMANI         SET EZ-180-YES TO TRUE
ZMANI         COMPUTE I = CT155-TVL-IND  + 1
ZMANI      END-IF.
ZMANI  99Z-CONT.
ZMANI  99Z-EXIT.
ZMANI      EXIT.
 