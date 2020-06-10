
## How to use

1. Pass a `ConfirmationDialog.Details` object in arguments with key `ConfirmationDialog.KEY_CONFIRMATION_DETAILS` when navigating to the fragment.
2. Calling fragments should observe `ConfirmationViewModel.result`.
    1. Only act on results where `result.callerTag` matches fragment's caller tag.
    2. Multiple confirmation requests from a fragment are identified using their `result.requestCode`.
    3. After consuming the result, call `ConfirmationViewModel.reset()`.
3. Colour of confirmation buttons is taken from app theme. It may be changed by adding an `alertDialogTheme` to app theme. Check the `styles.xml` in sample for how to. 

