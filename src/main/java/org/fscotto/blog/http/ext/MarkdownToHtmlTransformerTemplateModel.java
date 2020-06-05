/*
 * Copyright (C) 2019 Fabio Scotto di Santolo <fabio.scottodisantolo@gmail.com>
 *
 * This file is part of plague-blog.
 *
 * plague-blog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * plague-blog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with plague-blog.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.fscotto.blog.http.ext;

import com.vladsch.flexmark.ext.abbreviation.AbbreviationExtension;
import com.vladsch.flexmark.ext.admonition.AdmonitionExtension;
import com.vladsch.flexmark.ext.aside.AsideExtension;
import com.vladsch.flexmark.ext.attributes.AttributesExtension;
import com.vladsch.flexmark.ext.autolink.AutolinkExtension;
import com.vladsch.flexmark.ext.definition.DefinitionExtension;
import com.vladsch.flexmark.ext.emoji.EmojiExtension;
import com.vladsch.flexmark.ext.emoji.EmojiImageType;
import com.vladsch.flexmark.ext.emoji.EmojiShortcutType;
import com.vladsch.flexmark.ext.enumerated.reference.EnumeratedReferenceExtension;
import com.vladsch.flexmark.ext.footnotes.FootnoteExtension;
import com.vladsch.flexmark.ext.gfm.issues.GfmIssuesExtension;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.gfm.users.GfmUsersExtension;
import com.vladsch.flexmark.ext.gitlab.GitLabExtension;
import com.vladsch.flexmark.ext.media.tags.MediaTagsExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.ext.typographic.TypographicExtension;
import com.vladsch.flexmark.ext.youtube.embedded.YouTubeLinkExtension;
import com.vladsch.flexmark.formatter.Formatter;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.parser.PegdownExtensions;
import com.vladsch.flexmark.profiles.pegdown.PegdownOptionsAdapter;
import com.vladsch.flexmark.util.ast.KeepType;
import com.vladsch.flexmark.util.data.DataSet;
import com.vladsch.flexmark.util.data.MutableDataSet;
import com.vladsch.flexmark.util.format.options.DiscretionaryText;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class MarkdownToHtmlTransformerTemplateModel implements TemplateMethodModelEx {

  @Override
  public String exec(List args) throws TemplateModelException {
    if (args.size() != 1) {
      throw new TemplateModelException("Wrong arguments");
    }
    var parser = Parser.builder(options()).build();
    var renderer = HtmlRenderer.builder(options()).build();
    return Stream.ofNullable(args)
      .filter(Objects::nonNull)
      .map(list -> new LinkedList(list))
      .flatMap(list -> Stream.of(list.getFirst()))
      .filter(Objects::nonNull)
      .filter(SimpleScalar.class::isInstance)
      .map(SimpleScalar.class::cast)
      .map(SimpleScalar::getAsString)
      .filter(Objects::nonNull)
      .filter(StringUtils::isNotBlank)
      .map(parser::parse)
      .map(renderer::render)
      .findFirst()
      .orElseGet(String::new);
  }

  private DataSet options() {
    return new MutableDataSet(PegdownOptionsAdapter.flexmarkOptions(PegdownExtensions.ALL).toMutable())
      .set(AttributesExtension.USE_EMPTY_IMPLICIT_AS_SPAN_DELIMITER, Boolean.TRUE)
      .set(Formatter.INDENTED_CODE_MINIMIZE_INDENT, Boolean.TRUE)
      .set(EmojiExtension.USE_SHORTCUT_TYPE, EmojiShortcutType.GITHUB)
      .set(EmojiExtension.USE_IMAGE_TYPE, EmojiImageType.IMAGE_ONLY)
      .set(TablesExtension.APPEND_MISSING_COLUMNS, Boolean.TRUE)
      .set(TablesExtension.COLUMN_SPANS, Boolean.FALSE)
      .set(TablesExtension.DISCARD_EXTRA_COLUMNS, Boolean.TRUE)
      .set(TablesExtension.FORMAT_TABLE_LEFT_ALIGN_MARKER, DiscretionaryText.ADD)
      .set(TablesExtension.FORMAT_TABLE_CAPTION_SPACES, DiscretionaryText.ADD)
      .set(TablesExtension.HEADER_SEPARATOR_COLUMN_MATCH, Boolean.TRUE)
      .set(TablesExtension.MIN_HEADER_ROWS, 1)
      .set(TablesExtension.MAX_HEADER_ROWS, 1)
      .set(TablesExtension.WITH_CAPTION, Boolean.FALSE)
      .set(HtmlRenderer.INDENT_SIZE, 4)
      .set(HtmlRenderer.PERCENT_ENCODE_URLS, Boolean.TRUE)
      .set(Parser.REFERENCES_KEEP, KeepType.LAST)
      .set(Parser.EXTENSIONS, List.of(
        AbbreviationExtension.create(),
        AdmonitionExtension.create(),
        AsideExtension.create(),
        AttributesExtension.create(),
        AutolinkExtension.create(),
        DefinitionExtension.create(),
        FootnoteExtension.create(),
        EmojiExtension.create(),
        EnumeratedReferenceExtension.create(),
        GfmIssuesExtension.create(),
        GfmUsersExtension.create(),
        GitLabExtension.create(),
        MediaTagsExtension.create(),
        StrikethroughExtension.create(),
        TablesExtension.create(),
        TypographicExtension.create(),
        YouTubeLinkExtension.create()
      ));
  }

}
