<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
  <html>
  <head>
	<style>
		body{
			font-family: "Courier New";
		}
	</style>
  </head>
  <body>
      <xsl:for-each select="snapshot/row">
	  <table style="width:800px;border:0px">
        <tr>
	      <xsl:for-each select="field">
	        <td>
	        	<xsl:value-of select="translate(@value,' ','_')" />
	        </td>
	      </xsl:for-each>
        </tr>
	  </table>
      </xsl:for-each>
  </body>
  </html>
</xsl:template>

</xsl:stylesheet>