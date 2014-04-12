           03 ES_SUG_USER              PIC 9(2).
           03 ES_MHOZ                  PIC 9(2).
           03 ES_TV_MEHOZOT.
              05 ES_TV_MEHOZOT_O OCCURS 5.
                 07 ES_MHZN            PIC 9(2).
                 07 ES_SYS_OPEN_CLOSE  PIC X.
           03 ES_SUM_MHOZ              PIC 9(3).
           03 ES_USER_NAME             PIC X(10).
           03 ES_KOD_MAKISH            PIC 9(3).
           03 ES_TV_SOCHNIM.
              05 ES_TV_SOCHNIM_O OCCURS 500.
                 07 ES_SOCHEN          PIC 9(6).
                 07 ES_SOCHEN_NAME     PIC X(30).
 YDD             07 ES_SOCHEN_TZ       PIC 9(9).
      ***                           ֵָ־׀ ֱַֻֽ = 0   ֵָ־׀ ¡ָ ֱַֻֽ = 1
 YDD  ***        07 ES_SOCHEN_LO_PAIL  PIC 9.
                 07 ES_SOCHEN_ADDRS    PIC X(30).
                 07 ES_SOCHEN_TEL      PIC 9(9).
                 07 ES_SOCHEN_MAIL     PIC X(40).
      *---                                           ּֽ¿ֺ װֱ×־ ×/¡ ֱַֻֽ
                 07 ES_SOCHEN_AB       PIC X.
           03 ES_SUM_SOCHEN            PIC 9(3).
           03 ES_TV_ANAFIM.
  ********                   50 - ֵ¿ֵ״־ ױֱֵֺױָ ֶ¡ , 25 ¿־ ״ֱֵַּ״× ױֱֵֺױ
              05 ES_TV_ANAFIM_O  OCCURS 50.
   ********      07 ES_MHOZ_ANAF       PIC 9(2).
                 07 ES_ANAF            PIC 9(3).
                 07 ES_QWRY            PIC X.
                 07 ES_CHGA            PIC X.
                 07 ES_IMMD            PIC X.
                 07 ES_CHSV            PIC X.
                 07 ES_ISHR            PIC X.
                 07 ES_AMLA_PREMIUM_UPDATE_B PIC X.
                 07 ES_AMLA_ARSHAA_READ_A    PIC X.
                 07 ES_AMLA_ARSHAA_UPDATE_A  PIC X.
                 07 ES_AMLA_ARSHAA_READ_B    PIC X.
                 07 ES_AMLA_ARSHAA_UPDATE_B  PIC X.
                 07 ES_MISHNE_UPDATE_B       PIC X.
                 07 ES_MESHUTAF_UPDATE_B     PIC X.
                 07 ES_MASAX_PREMIUMIM       PIC X.
                 07 ES_MASAX_SHEABUD         PIC X.
           03 ES_SUM_ANAF             PIC 9(2).
           03 ES_ANAF_999             PIC X(1).
 YDD       03 ES_USER_HATAM           PIC X(10).
 YDD       03 ES_NAME_HATAM           PIC X(30).
           03 ES_SOC_MADANES           PIC X.
 ISO       03 ES_ISOH_BDIKA            PIC 9.
 ISO       03 ES_ISOH_SAP              PIC 9.
 ISO       03 ES_ISOH_ISHUR_MEUHAD     PIC 9.
 ELAL      03 ES_ZEVET_AL              PIC 9.
 ISO       03 ES_ISOH_PATUR            PIC 9.
           03 ES_IMAGE                 PIC 9.
 ISO       03 ES_HAZHARA               PIC X.
           03 ES_DMAI_POLISA_TOS_INF   PIC X.

