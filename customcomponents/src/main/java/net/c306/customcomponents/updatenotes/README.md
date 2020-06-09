
## HOW TO USE
1. Preferably use with navigation component.
2. Before navigating to fragment, set title and notes resource Id in [UpdateNotesViewModel].
3. To enable javascript interaction (day/night theme, link colour, padding), update javascript
interface to use `c306_custom_components`

## HOW TO CREATE A UPDATE NOTES FILE
1. Copy contents of `/updatenotes` to root of project folder
2. Write update notes in the `updatenotes.md` file.
3. Run gulp file—it will generate updatenotes.html, adding js and css, and copy it to raw resources folder
