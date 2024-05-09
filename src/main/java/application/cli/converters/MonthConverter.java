package application.cli.converters;

import application.models.Month;

import picocli.CommandLine;

public class MonthConverter implements CommandLine.ITypeConverter<Month> {

    @Override
    public Month convert(String value) throws Exception {
        try {
            return Month.valueOf(value.toUpperCase()); 
        } catch (IllegalArgumentException e) {
            throw new CommandLine.TypeConversionException("Wrong month name: " + value + ". Avaliable month names: " + Month.getAllDisplayNames());
        }
    }


}
