<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl= "http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:template match="/">
    <xsl:apply-templates select="%s" />
  </xsl:template>

  <xsl:template match="book">
    <book xmlns="http://xmllibrary.gregorias.me/Library">
      <xsl:copy-of select="descendant::node()" />
      <xsl:apply-templates select="//item[isbn-10=current()/isbn-10]" />
    </book>
  </xsl:template>

  <xsl:template match="item">
    <xsl:copy-of select="self::node()" />
  </xsl:template>
</xsl:stylesheet>
