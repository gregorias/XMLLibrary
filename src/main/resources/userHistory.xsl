<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl= "http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:template match="/">
    <history xmlns="http://xmllibrary.gregorias.me/Library">
      <xsl:apply-templates select="//item[history/rent[renteeId=%d]]" />
    </history>
  </xsl:template>

  <xsl:template match="item">
    <xsl:variable name="isbn-10" select="isbn-10"/>
    <historyItem>
      <xsl:for-each select="history/rent[renteeId=%d]">
        <title>
          <xsl:value-of select="//book[isbn-10=$isbn-10]/title" />
        </title>
        <xsl:copy-of select="." />
      </xsl:for-each>
    </historyItem>
  </xsl:template>
</xsl:stylesheet>
