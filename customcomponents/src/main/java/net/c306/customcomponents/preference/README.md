## Resource properties to customise appearance

### SearchableListPreference

#### Styling

Colour of hint text in search field: `@color/text_color_hint_search_box`
Colour of search field underline on focus: `@color/stroke_color_search_box`

List item background states:  
`@color/bg_searchable_list_item_activated`  
`@color/bg_searchable_list_item_focused`  
`@color/bg_searchable_list_item_normal`  
`@color/bg_searchable_list_item_disabled`  

Colour of optional divider after list item: `@color/upgraded_list_divider`


#### Configuration attributes

**`defaultValues`** (`String?`): Optional default values. Separate multiple values, if multi-select is enabled, with a comma.  
**`showSearch`** (`Boolean`): Whether to show search box in preference dialog. Default `true`.  
**`enableMultiSelect`** (`Boolean`): Whether to use in single select or multi-select mode Default `true`.
**`message`** (`String?`): Optional message to display at bottom of list in preference dialog.  
**`disabledSummary`** (`CharSequence?`): Optional summary to display when preference is disabled.  
**`emptyViewText`** (`String?`): Optional string to display in preference dialog when there are no items.  
**`noneSelectedSummary`** (`String?`): Optional summary to display when preference is enabled but no entry is selected.  

All attributes may also be specified in xml. E.g.,
```xml
app:multiSelect="false"
```

Entries are provided in code in form of a `TypedArray` with `SearchableListPreference.Entry` values.

#### SearchableListPreference.Entry

**`entry`** (`String`): String that is displayed in list in dialog.  
**`value`** (`String`): Value that's saved to storage, equivalent of entryValues in ListPreference.  Defaults to [entry].  
**`summary`** (`String`): Optional display name to show in summary. Defaults to [entry].  
**`listSearchString`** (`String`): Optional search string to use for searching. Defaults to [entry].   
**`enabled`** (`Boolean`): Whether this entry is enabled for selection. Default is `true`.  
**`dividerBelow`** (`Boolean`): Enable to show divider below this entry in list Default is `false`. Useful for grouping entries.  

### TimePreference

#### Styling

Accent colour used for clock and header: `@color/accent_time_date_dialog`
