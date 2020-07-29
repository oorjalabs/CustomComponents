## Resource properties to customise appearance

### SearchableMultiSelectPreference

Colour of hint text in search field: `@color/text_color_hint_search_box`
Colour of search field underline on focus: `@color/stroke_color_search_box`

List item background states:  
`@color/bg_searchable_list_item_activated`  
`@color/bg_searchable_list_item_focused`  
`@color/bg_searchable_list_item_normal`  
`@color/bg_searchable_list_item_disabled`  


### UpgradedListPreferenceDialog

Background of selected list item: `@color/bg_list_item_activated`

Colour of optional divider after list item: `@color/upgraded_list_divider`
    
    
### TimePreference

Accent colour used for clock and header: `@color/accent_time_date_dialog`

## Optional in-code attributes

### SearchableMultiSelectPreference

**`message`** (`String?`): Optional message to display at bottom of list in preference dialog.  
**`disabledSummary`** (`CharSequence?`): Optional summary to display when preference is disabled.  
**`emptyViewText`** (`String?`): Optional string to display in preference dialog when there are no items.  
**`noneSelectedSummary`** (`String?`): Optional summary to display when preference is enabled but no entry is selected.  
 

### UpgradedListPreferenceDialog

**`message`** (`String?`): Optional message to display at bottom of list in preference dialog.  
**`disabledSummary`** (`CharSequence?`): Optional summary to display when preference is disabled.  
    