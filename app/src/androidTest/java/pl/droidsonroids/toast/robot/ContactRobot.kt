package pl.droidsonroids.toast.robot

import pl.droidsonroids.toast.R

class ContactRobot : BaseRobot() {

    fun goToContactScreen() {
        with(ContactRobot()) {
            performClickOnElementWithId(R.id.actionContact)
        }
    }

    fun checkIfSendButtonIsNotClickable() {
        checkIfElementWithIdIsVisible(R.id.disabledSendButton)
        checkIfElementWithIdIsNotVisible(R.id.enabledSendButton)
    }
}