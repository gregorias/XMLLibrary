<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl= "http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:template match="/">
<html>
<body style="padding: 10px;">
  <xsl:for-each select="%s">
    <div style="border-style: solid;
        border-width: 2px;
        border-color: #000000;
        margin: 10px;
        padding: 10px; ">
      <h1><xsl:value-of select="title"/></h1>
      <p>
      <xsl:value-of select="description"/>
      </p>
      <div>
        <h3> Authors </h3>
        <ul>
        <xsl:for-each select="authors/author">
          <li><xsl:value-of select="."/></li>
        </xsl:for-each>
        </ul>
      </div>
      <div>
        <h3> Edition </h3>
        <xsl:value-of select="edition"/>
      </div>
      <div>
        <h3> Publisher </h3>
        <xsl:value-of select="publisher"/>
      </div>
      <div>
        <h3> isbn10 </h3>
        <xsl:value-of select="isbn-10"/>
      </div>
      <xsl:variable name="currentISBN" select="isbn-10" />
      <div>
        <h3> Items </h3>
        <ul>
          <xsl:for-each select="//item[isbn-10=$currentISBN]">
            <li> <xsl:apply-templates select="."/> </li>
          </xsl:for-each>
        </ul>
      </div>
    </div>
  </xsl:for-each>
</body>
</html>
  </xsl:template>
  <xsl:template match="item">
    <h4> Status </h4>
    <xsl:value-of select="status" />
  </xsl:template>

</xsl:stylesheet>
