package HR_Mudol.domain;

public class EmploymentContract{
        private int minDayShift; //Minimum Number of Day Shifts per Week
        private int minEveninigShift; //Minimum Number of Eveninig Shifts per Week
        private int sickDays; //how many remained
        private int daysOff; //how many remained

        public EmploymentContract(int minDayShift, int minEveninigShift, int sickDays, int daysOff) {

            this.minDayShift = minDayShift;
            this.minEveninigShift = minEveninigShift;
            this.sickDays = sickDays;
            this.daysOff = daysOff;
        }

        public int getMinDayShift(User caller, Employee employee) {

            if (!caller.isManager()||!caller.isSameEmployee(employee)) {
                throw new SecurityException("Access denied");
            }
            return minDayShift;
        }

        public void setMinDayShift(User caller, int minDayShift) {
            if (!caller.isManager()) {
                throw new SecurityException("Access denied");
            }
            this.minDayShift = minDayShift;

        }

    public int getMinEveninigShift(User caller, Employee employee) {

        if (!caller.isManager()||!caller.isSameEmployee(employee)) {
            throw new SecurityException("Access denied");
        }
        return minEveninigShift;
    }

    public void setMinEveninigShift(User caller, int minEveninigShift) {
        if (!caller.isManager()) {
            throw new SecurityException("Access denied");
        }
        this.minEveninigShift = minEveninigShift;

    }
        public int getSickDays(User caller,Employee employee) {
            if (!caller.isManager()||!caller.isSameEmployee(employee)) {
                throw new SecurityException("Access denied");
            }
            return sickDays;
        }

        public void setSickDays(User caller,int sickDays) {
            if (!caller.isManager()) {
                throw new SecurityException("Access denied");
            }
            this.sickDays = sickDays;
        }

        public int getDaysOff(User caller, Employee employee) {
            if (!caller.isManager()||!caller.isSameEmployee(employee)) {
                throw new SecurityException("Access denied");
            }
            return daysOff;
        }

        public void setDaysOff(User caller,int daysOff)
        {
            if (!caller.isManager()) {
                throw new SecurityException("Access denied");
            }
            this.daysOff = daysOff;
        }
        @Override
        public String toString() {
            return "  Contract details:" +
                    "\n  Minimum evening shifts at week: " + this.minEveninigShift +
                    "\n  Minimum day shifts at week: " + this.minDayShift +
                    "\n  sick Days that remained: " + this.sickDays +
                    "\n  Days off that remained: " + this.daysOff;
        }
    }

