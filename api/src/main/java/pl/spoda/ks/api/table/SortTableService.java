package pl.spoda.ks.api.table;

import org.springframework.stereotype.Service;
import pl.spoda.ks.api.season.enums.PointCountingMethod;
import pl.spoda.ks.database.dto.TableBaseDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

@Service
public class SortTableService {


    public <T extends TableBaseDto> List<T> getSortedTable(List<T> tableData, PointCountingMethod pointCountingMethod
            , boolean isNewMatch) {
        Function<TableBaseDto, BigDecimal> pointsCountingComparator = getComparingFunction( pointCountingMethod );
        List<T> sortedTable = new ArrayList<>( tableData ).stream()
                .sorted( Comparator
                        .comparing( pointsCountingComparator, Comparator.naturalOrder() ).reversed()
                        .thenComparing( row -> row.getPlayer().getAlias(),
                                Collator.getInstance( new Locale( "pl" ) ) ) )
                .toList();


        for (int i = 0; i < sortedTable.size(); i++) {
            if (isNewMatch) {
                sortedTable.get( i ).setStandbyPosition( sortedTable.get( i ).getPreviousPosition() );
                sortedTable.get( i ).setPreviousPosition( sortedTable.get( i ).getCurrentPosition() );
            }
            sortedTable.get( i ).setCurrentPosition( i + 1 );
        }

        return sortedTable;
    }

    private <T extends TableBaseDto> Function<T, BigDecimal> getComparingFunction(PointCountingMethod pointCountingMethod) {
        return switch (pointCountingMethod) {
            case RATING -> TableBaseDto::getCurrentRating;
            case POINTS_TOTAL -> TableBaseDto::getPointsTotal;
            case POINTS_PER_MATCH -> x -> x.getMatches().divide( x.getPointsTotal(), RoundingMode.HALF_UP );
        };
    }

}
