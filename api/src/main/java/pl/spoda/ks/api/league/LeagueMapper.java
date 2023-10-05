package pl.spoda.ks.api.league;

import org.springframework.stereotype.Service;
import pl.spoda.ks.api.league.enums.LeagueType;
import pl.spoda.ks.api.league.enums.TeamStructure;
import pl.spoda.ks.api.league.model.LeagueData;
import pl.spoda.ks.api.league.model.LeagueRequest;
import pl.spoda.ks.database.dto.LeagueDto;

import java.util.List;

@Service
public class LeagueMapper {
    public LeagueDto mapLeague(LeagueRequest request) {

        return LeagueDto.builder()
                .name( request.getName() )
                .description( request.getDescription() )
                .teamStructure( request.getTeamStructure().name() )
                .type( request.getType().name() )
                .build();
    }

    public List<LeagueData> mapLeagueList(List<LeagueDto> storedLeagues) {
        return storedLeagues.stream()
                .map( this::mapToLeagueData )
                .toList();
    }

    private LeagueData mapToLeagueData(LeagueDto leagueDto) {
        return LeagueData.builder()
                .leagueId( leagueDto.getId() )
                .description( leagueDto.getDescription() )
                .name( leagueDto.getName() )
                .type( LeagueType.getByName( leagueDto.getType()) )
                .teamStructure( TeamStructure.getByName(  leagueDto.getTeamStructure() ))
                .build();

    }
}
