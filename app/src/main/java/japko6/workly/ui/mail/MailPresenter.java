package japko6.workly.ui.mail;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import japko6.workly.objects.DateKey;
import japko6.workly.objects.Day;
import japko6.workly.objects.Time;
import japko6.workly.prefs.Prefs;
import japko6.workly.ui.base.BasePresenter;
import japko6.workly.utils.CalendarUtils;

public class MailPresenter extends BasePresenter<MailFragment> {
    private int yearStart;
    private int monthStart;
    private int dayStart;

    private int yearStop;
    private int monthStop;
    private int dayStop;

    protected interface view {

        void showDatePicker(final MailFragment.date d);

        void setMailEt(String mail);

        void setTimeStringFrom(int year, int month, int day);

        void setTimeStringTo(int year, int month, int day);

        void showWrongDateInfoFrom();

        void showWrongDateInfoTo();

        void showEmptyDaysInfo();

        void showNoMailClientInfo();

        void showNoMailEnteredInfo();

        void notValidMailInfo();

        String getMailSubjectText();

        String getTitleOfMailIntent();

        String getMail();
    }

    @Override
    public void onLoad(MailFragment view) {
        super.onLoad(view);
        this.dayStart = 1;
        this.monthStart = CalendarUtils.getActualMonth();
        this.yearStart = CalendarUtils.getActualYear();
        this.dayStop = CalendarUtils.getActualDay();
        this.monthStop = CalendarUtils.getActualMonth();
        this.yearStop = CalendarUtils.getActualYear();

        getView().setTimeStringFrom(yearStart, monthStart, dayStart);
        getView().setTimeStringTo(yearStop, monthStop, dayStop);
    }

    public void onButtonSend() {
        if (validateDate()) {
            sendMail();
        } else {
            getView().showWrongDateInfoFrom();
            getView().showWrongDateInfoTo();
        }
    }

    protected boolean validateDate() {
        if (yearStop < yearStart) {
            return false;
        }

        if (yearStop > yearStart) {
            return true;
        }

        if (yearStart == yearStop) {
            if (monthStart > monthStop) {
                return false;
            }

            if (monthStart < monthStop) {
                return true;
            }

            if (monthStart == monthStop) {
                if (dayStart == dayStop) {
                    return true;
                }

                if (dayStart > dayStop) {
                    return false;
                }

                if (dayStart < dayStop) {
                    return true;
                }
            }
        }

        return true;
    }

    public boolean isValidEmailAddress(String email) {
        email = email.replace(" ", "");
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    private void sendMail() {
        if (TextUtils.isEmpty(getView().getMail().replace(" ", ""))) {
            getView().showNoMailEnteredInfo();
            return;
        } else {
            if (isValidEmailAddress(getView().getMail())) {
                Prefs.setEmail(getView().getMail());
            } else {
                getView().notValidMailInfo();
                return;
            }
        }

        ArrayList<Day> days = Prefs.getDays();
        DateKey startDate = new DateKey(this.yearStart, this.monthStart, this.dayStart);
        DateKey stopDate = new DateKey(this.yearStop, this.monthStop, this.dayStop);

        ArrayList<Day> filteredArrayList = new ArrayList<>();
        if (days == null) {
            getView().showEmptyDaysInfo();
            return;
        }

        Integer positionStart = null, positionStop = null;

        for (int i = 0; i < days.size(); i++) {
            if (days.get(i).getDate().equals(startDate)) {
                positionStart = i;
            }

            if (days.get(i).getDate().equals(stopDate)) {
                positionStop = i;
            }
        }

        if (positionStart != null && positionStop != null) {
            for (int i = positionStart; i <= positionStop; i++) {
                if (i == positionStop) {
                    if (Prefs.isWorking()) {
                        if (days.get(i).getWorkIntervals().get(days.get(i).getWorkIntervals().size() - 1).getStopInterval() == null) {
                            days.get(i).getWorkIntervals()
                                    .get(days.get(i).getWorkIntervals().size() - 1)
                                    .setStopInterval(new Time());
                        }
                    }
                }
                filteredArrayList.add(days.get(i));
            }
        } else {
            getView().showEmptyDaysInfo();
            return;
        }


        if (filteredArrayList.size() > 0) {
            if (!isMailClientIsInstalled()) {
                getView().showNoMailClientInfo();
                return;
            } else {
                String mail = Prefs.getEmail();
                if (mail.equals("")) {
                    getView().showNoMailEnteredInfo();
                    return;
                } else {
                    String dayStart;
                    String monthStart;
                    String yearStart = String.valueOf(this.yearStart);
                    String dayStop;
                    String monthStop;
                    String yearStop = String.valueOf(this.yearStop);

                    if (this.dayStop < 10) {
                        dayStop = "0" + String.valueOf(this.dayStop);
                    } else {
                        dayStop = String.valueOf(this.dayStop);
                    }
                    if (this.dayStart < 10) {
                        dayStart = "0" + String.valueOf(this.dayStart);
                    } else {
                        dayStart = String.valueOf(this.dayStart);
                    }
                    if (this.monthStart + 1 < 10) {
                        monthStart = "0" + String.valueOf(this.monthStart + 1);
                    } else {
                        monthStart = String.valueOf(this.monthStart + 1);
                    }
                    if (this.monthStop + 1 < 10) {
                        monthStop = "0" + String.valueOf(this.monthStop + 1);
                    } else {
                        monthStop = String.valueOf(this.monthStop + 1);
                    }

                    String subject = getView().getMailSubjectText() + " " +
                            dayStart + "." +
                            monthStart + "." +
                            yearStart + " - " +

                            dayStop + "." +
                            monthStop + "." +
                            yearStop;

                    String reportContent = CalendarUtils.prepareReportString(filteredArrayList, getView().getContext());
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("plain/text");
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{mail});
                    intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                    intent.putExtra(Intent.EXTRA_TEXT, reportContent);

                    getView().startActivity(Intent.createChooser(intent, getView().getTitleOfMailIntent()));
                    return;
                }
            }
        } else {
            getView().showEmptyDaysInfo();
        }
    }


    public boolean isMailClientIsInstalled() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        final PackageManager packageManager = getView().getContext().getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

        return list.size() != 0;
    }

    protected void onDateFromChangePressed(MailFragment.date e) {
        getView().showDatePicker(e);
    }

    protected void saveEmailInPrefs(String mail) {
        Prefs.setEmail(mail);
        getView().setMailEt(mail);
    }

    protected void onDateSelectedPressed(int year, int month, int day, MailFragment.date e) {
        if (e == MailFragment.date.FROM) {
            this.dayStart = day;
            this.monthStart = month;
            this.yearStart = year;
            getView().setTimeStringFrom(year, month, day);
        } else {
            this.dayStop = day;
            this.monthStop = month;
            this.yearStop = year;
            getView().setTimeStringTo(year, month, day);
        }
    }

    @Nullable
    protected DateKey getFirstDayOfWork() {
        ArrayList<Day> days = Prefs.getDays();

        if (days != null) {
            return days.get(0).getDate();
        } else return null;
    }
}
