<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl= "http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:template match="/">
    <book>
    <xsl:for-each select="%s">
      <xsl:copy-of select="descendant::node()" />
      <xsl:variable name="currentISBN" select="isbn-10" />
      <xsl:for-each select="//item[isbn-10=$currentISBN]">
        <xsl:copy-of select="self::node()" />
      </xsl:for-each>
    </xsl:for-each>
    </book>
  </xsl:template>
</xsl:stylesheet>
